/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

//import ch.unige.cui.asg.android.hoverinfo.middleware.Logging;
import ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService;
import ch.unige.cui.asg.android.hoverinfo.middleware.Logging;
import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Area;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Buffer;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.CircularArea;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.CleaningTask;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Content;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.HoverID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Name;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Replica;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.BBAReplicaPacket;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.Network;

/**
 * @author alfredo
 *
 */
public class BBAStorage extends StorageAlgorithm {

	public static final String TAG = "BBAStorage";

	protected Buffer bufferModule = null;
	protected Network networkModule = null;
	
	
	public Timer replTimer = null; 

	
	
	//--------------------------------
	// Constructors
	//--------------------------------
	
	private BBAStorage() {}
	private static BBAStorage instance = null;
	public static BBAStorage getInstance() {
		if (instance == null) instance = new BBAStorage();
		return instance;
	}


	//-------------------------------------------
	// Initialization
	//-------------------------------------------

	@Override
	public void initialize() {

		// Get other modules
		
		bufferModule = Buffer.getInstance();
		networkModule = Network.getInstance();
		
		// Create threads
		
		replTimer = new Timer("Replicator Timer");
		
	}


	//-------------------------------------------
	// storage interface
	//-------------------------------------------
	
	public void store(MobApplID mobApplID, String name, String content, double anchorRadius,
			long ttl) {
			
				HoverID hoverID = HoverID.genHoverID(HoverinfoService.nodeID, mobApplID, name);  // TODO create hoverID from mobApplID, name, and nodeID
				
				// check if name already exists
				
				// check if buffer is full
				
				// insert <name, value> into the buffer
				
				Replica h = new Replica();
				h.setHoverID(hoverID);
				h.setName(new Name(name));
				h.setContent(new Content(content));
				h.setAnchorArea(
						new CircularArea(
								HoverinfoService.geoloc.getCurrentLocation(),
								anchorRadius)); 
				h.setTtl(ttl);
				
				insertNewReplica(h);
				
				Log.v(TAG, "hoverinfo stored");
				Logging.log(TAG, "hoverinfo stored " +
				        "(id=" + h.getHoverID().getValue() + ", " +
				        "content=" + h.getContent().getValue() + ", "+
				        "AH=" + h.getAnchorArea().toString() + ")");
			
				// populate
				
				populate(h);
			}

	
	public void insertNewReplica(Replica h) {
		
		// insert a new replica into the buffer
		
		HoverinfoService.buffer.insert(h);
		
		// schedule replication
		
		if (HoverinfoService.buffer.size() == 1) {
	
			replTimer.scheduleAtFixedRate(
					new TimerTask() {
						@Override
						public void run() {
							replicate();
						}
					},
					BBAParameters.REPLICATION_TIMER,
					BBAParameters.REPLICATION_TIMER
					);
			
			Buffer.cleanThread.scheduleAtFixedRate(
					new CleaningTask(),
					HoverinfoService.CLEANING_TIMER,
					HoverinfoService.CLEANING_TIMER
					);
		}
		
		// metrics
		//....
	}

	
	/*==============================================================*
	 |                                                              |
	 |                 P O P U L A T I O N                          |
	 |                                                              |
	 *==============================================================*/
	

	
	public void populate(Replica h) {
	    
	    Logging.log(TAG, "init populate (hid=" + h.getHoverID()+ ")");
		
		BBAReplicaPacket m = new BBAReplicaPacket();
		
		m.setHoverID(h.getHoverID());
		m.setName(h.getName());
		m.setValue(h.getContent());
		m.setAnchorArea(h.getAnchorArea());
		m.setTtl(h.getTtl());
		
		m.setPopulate();
		m.setSrcLoc(HoverinfoService.geoloc.getCurrentLocation());
		
		networkModule.bcast(m);
	}
	

	public void recvPopulatePkt(BBAReplicaPacket m) {
		
		if (!m.isPopulate()) {
			Log.e("BBAStorage", "we expected an incoming populate packet - some logic error");
			return;
		}
		
		// check if the replica is already stored
		
		Logging.log(TAG, "receive populate pkt (hid=" + m.getHoverID() + ")");
		
		if (bufferModule.getReplica(m.getHoverID()) != null) {
			
			// metric
			
			return;
		}
		
		// insert replica

        Logging.log(TAG, "insert new replica from populate pkt (hid=" + m.getHoverID() + ")");

		Replica h = new Replica();
		h.setHoverID(m.getHoverID());
		h.setName(m.getName());
		h.setContent(m.getValue());
		h.setAnchorArea(m.getAnchorArea());
		h.setTtl(m.getTtl());
		
		insertNewReplica(h);
		
		
		// forward the packet
 	
		double p = 0; // TODO choose randomely this value
		
		if (p < BBAParameters.PROB_FLOODING_POPULATE) {
			
			double dist = 0;  // TODO: compute distance between sender and node
			
			if (dist >= BBAParameters.MIN_FLOODING_POPULATE_DISTANCE_THRESHOLD) {
				
				BBAReplicaPacket m2 = new BBAReplicaPacket();
				
				m2.setHoverID(h.getHoverID());
				m2.setName(h.getName());
				m2.setValue(h.getContent());
				m2.setAnchorArea(h.getAnchorArea());
				m2.setTtl(h.getTtl());
				
				m2.setPopulate();
				m2.setSrcLoc(HoverinfoService.geoloc.getCurrentLocation());
	
				networkModule.bcast(m2);
			}
		}
	}
	
	
	
	/*==============================================================*
	 |                                                              |
	 |                  R E P L I C A T I O N                       |
	 |                                                              |
	 *==============================================================*/
	
	/**
	 * This methods should run in the replicator thread.
	 */
	public void replicate() {
		
		Log.v(TAG, "periodical check for replication");
        Logging.log(TAG, "periodical check for replication");
		
		Iterator<Replica> it = HoverinfoService.buffer.iterator();
		for(; it.hasNext();) {
			Replica h = it.next();
			
			if (HoverinfoService.geoloc.isNodeInside(h.getAnchorArea())) {
				
				replicateReplica(h);
				
			}
			
		}
	}

	/**
	 * This method should run in the replicator thread.
	 */
	public void replicateReplica(Replica h) {


		// create replica packet and broadcast it
		
		BBAReplicaPacket m = new BBAReplicaPacket();
		
		m.setHoverID(h.getHoverID());
		m.setName(h.getName());
		m.setValue(h.getContent());
		m.setAnchorArea(h.getAnchorArea());
		m.setTtl(h.getTtl());
		
		m.setSrcLoc(HoverinfoService.geoloc.getCurrentLocation());
	
		HoverinfoService.network.bcast(m);
		
		Log.v(TAG, "replica replicated");
		Logging.log(TAG, "replica replicated (hid=" + h.getHoverID() + ")");
		
		// metrics
		// ....
	}

	/**
	 * This methods should run in the receiver thread.
	 */
	public void recvReplicaPkt(BBAReplicaPacket m) {
		
		Log.v(TAG, "replica pkt rcvd hoverID:"+m.getHoverID());
        Log.v(TAG, "replica pkt rcvd (hid="+m.getHoverID() +")");
	
		// check is the incoming replica is already in the buffer
		
		HoverID hoverID = m.getHoverID();
		
		if (HoverinfoService.buffer.getReplica(hoverID) != null) {
			
			// replica already in the buffer
			// ......
			
			// metrics
			// .....
			
			return;
			
		}
	
		// insert replica into the buffer


		Replica h = new Replica();
		h.setHoverID(m.getHoverID());
		h.setName(m.getName());
		h.setContent(m.getValue());
		h.setAnchorArea(m.getAnchorArea());
		h.setTtl(m.getTtl());
	
		insertNewReplica(h);

       Logging.log(TAG, "insert new replica from replica pkt (hid=" + m.getHoverID() + ")");

		// metrics ...
		// ..
	}

}
