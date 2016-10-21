package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;

import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService;
import ch.unige.cui.asg.android.hoverinfo.middleware.Measurable;
import ch.unige.cui.asg.android.hoverinfo.middleware.retrieval.Retrieval;

public class Buffer implements Measurable {
	
	
	public static Timer cleanThread = null; 


	private long maxSize;
	private HashMap<HoverID, Replica> replicas;
	
	
	private static Buffer instance = null;

	
	//--------------------------------------
	// Constructors
	//--------------------------------------
	
	private Buffer(long maxSize) {
		this.maxSize = maxSize;
		this.replicas = new HashMap<HoverID, Replica>((int) maxSize);
	}
	
	public static Buffer getInstance() {
		if (instance == null) instance = new Buffer(HoverinfoService.BUFFER_MAX_SIZE);
		return instance;
	}	


	
	
	//--------------------------------------
	// Access methods
	//--------------------------------------
	

	public Replica getReplica(HoverID ID) {
		Replica h = replicas.get(ID);
		return h;
	}
	
	public Iterator<Replica> iterator() {
		return replicas.values().iterator();
	}
	
	
	//--------------------------------------
	// Insertion methods
	//--------------------------------------

	public Replica insert(HoverID hoverID, String name, String value, Area anchorArea, long ttl) {
		Replica h = new Replica(hoverID, new Name(name), new Content(value), anchorArea, ttl);
		return insert(h);
	}
	
	
	public Replica insert(Replica h) {
		if (replicas.get(h.getHoverID()) != null) {
			Log.e("buffer", "trying to insert an alredy inserted replica - this shouldn't hapeen");
			// TODO: return an exception???
			return null;
		}
		if (replicas.size() > maxSize) return null;

		replicas.put(h.getHoverID(), h);
		Log.v("buffer", "replicas inserted, buffer size: " + replicas.size());
		
		Retrieval.pushBasedRetrieval.onNewReplicaInserted(h);
		
		return h;
	}

	
	
	//--------------------------------------
	// Disposal methods
	//--------------------------------------
	

	public void remove(String mobApplID, String name) {
	}


	public Replica remove(HoverID hoverID) {
		Replica h = replicas.remove(hoverID);
		Retrieval.pushBasedRetrieval.onReplicaRemoved(h);
		return h;
	}

	
	
	
	public int size() {
		return replicas.size();
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/*======================================*\
	 |                                      |
	 |            METRICS                   |
	 |                                      |
	\*======================================*/

	public static long numRepls;
	public static long numRmvdRepls;
	
	
	private String[] metrics =
	{
			"num replicas",
			"rmvd replicas"
	};

	public String[] getMetricsList() { return metrics; } 

	public double getMetricValue(int index) {
		
		switch (index) {
		
		case 0: return (double)numRepls;
		case 1: return (double)numRmvdRepls;
		
		}
		
		throw new IllegalArgumentException("metric index outside range");
	}

	


}
