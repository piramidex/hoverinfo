/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware;

import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Buffer;
import ch.unige.cui.asg.android.hoverinfo.middleware.clients.ClientsManager;
import ch.unige.cui.asg.android.hoverinfo.middleware.geolocalisation.GeoLocalisation;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.Network;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpPacketReceiver;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpPacketSender;
import ch.unige.cui.asg.android.hoverinfo.middleware.retrieval.SubscriptionsManager;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.ReplicasReceiverThread;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.Store;

/**
 * @author alfredo
 *
 */
public class HoverinfoService extends Service {

	private static final String TAG = "HoverinfoService";


	public static NodeID nodeID = new NodeID("1");
	
	
	// Global parameters and modules
	
	public static int		BUFFER_MAX_SIZE 							= 10;
//	public static long 		REPLICATION_TIMER 							= 10000;
	public static long 		CLEANING_TIMER 								= 60000;
	public static double 	CLEANING_DISTANCE 							= 100;
//	public static double 	MIN_FLOODING_POPULATE_DISTANCE_THRESHOLD 	= 80;
//	public static double 	PROB_FLOODING_POPULATE 						= 0.6;
	public static long 		LOCATION_UPDATES_INTERVAL 					= 1;


	public static LocationManager		locationManager = null;
	public static GeoLocalisation 		geoloc = null;
	public static Buffer 				buffer = null;
	public static Network 				network = null;
	public static Store 				storage = null;
	public static ClientsManager 		clientsManager = null;
	public static SubscriptionsManager 	subscriptionsManager = null;
	
	public static  HoverinfoService context;
	public static boolean isRunning = false;


	/**
	 * Creates the hoverinfo service.
	 */
	public void onCreate() {
		super.onCreate();
		
		Log.v(TAG, "onCreate");
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		//-------------------------------------
		// Initialize threads
		//-------------------------------------

		if (Buffer.cleanThread == null) Buffer.cleanThread = new Timer("Cleaning Thread");
//		if (Network.pktRecvThread == null) Network.pktRecvThread = new UdpPacketReceiver();
//		if (Store.recvThread == null) Store.recvThread = new ReplicasReceiverThread();
//		if (Store.replThread == null) Store.replThread = new Timer("Replicator Thread");

		//-------------------------------------
		// Initialize modules
		//-------------------------------------
		
		geoloc = GeoLocalisation.getInstance();
		buffer = Buffer.getInstance();
		network = Network.getInstance();
		storage = Store.getInstance();
		clientsManager = ClientsManager.getInstance();
		subscriptionsManager = SubscriptionsManager.getInstance();

		geoloc.initialize();
		buffer.initialize();
		network.initialize();
		storage.initialize();
		clientsManager.initialize();
		subscriptionsManager.initialize();

		//-------------------------------------
		// Initialize logging
		//-------------------------------------
		
		Logging.initialize(this);
		
		//-------------------------------------
		// Service status
		//-------------------------------------
		
		isRunning = true;
		
		context = this;
	}
	
	
	public void onDestroy() {
		super.onDestroy();
		
		Log.v(TAG, "onDestroy");
		
		//-------------------------------------
		// Stop threads
		//-------------------------------------
		
		Buffer.cleanThread.cancel();
		//Network.pktRecvThread.interrupt();
		
		storage.recvThread.interrupt();
//		storage.replThread.cancel();
		
		Bundle dataMsg = new Bundle();
		dataMsg.putBoolean("die", true);
		Message msg = Message.obtain();
		msg.setData(dataMsg);
//		Store.recvThread.handler.sendMessageDelayed(msg, 0);
		
		Buffer.cleanThread = null;
		network.pktRecvThread = null;
		storage.recvThread = null;
//		storage.replThread = null;
		
		
	}
	

	
	
	/*===========================================================*\
	 |                                                           |
	 |                    SERVICE INTERFACE                      |
	 |                                                           |
	\*===========================================================*/

	
	private IBinder binder = new HoverinfoServiceStubImpl();
	
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	

	/**
	 * Service stub 
	 */
	private class HoverinfoServiceStubImpl extends IHoverinfoService.Stub {

		@Override
		public String retrieve(String mobApplID, String name)
				throws RemoteException {
			
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void store(String mobApplID, String name, String content,
				double radius, long ttl) throws RemoteException {

			storage.store(new MobApplID(mobApplID), name, content, radius, ttl);
			
			// TODO: send to an storage thread ?
			
			
		}

		@Override
		public void subscribe(String mobApplID, double radiusOfInterest)
				throws RemoteException {
			
			subscriptionsManager.registerNewSubscription(new MobApplID(mobApplID));
			
			// TODO: send to another thread?
			
		}

		@Override
		public void registerCallback(String mobApplID, IHoverinfoServiceCallback callback) throws RemoteException {
			
			clientsManager.registerCallback(new MobApplID(mobApplID), callback);
			
		}
	}

}
