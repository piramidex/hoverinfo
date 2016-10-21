/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.storage;

import java.nio.channels.ClosedByInterruptException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.HoverID;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.BBAReplicaPacket;

/**
 * @author alfredo
 *
 */
public class ReplicasReceiverThread extends Thread {

	public Handler handler = null;
	
	public ReplicasReceiverThread() {
		super("Replicas Receiver Thread");
	}
	
	public void run() {

		Looper.prepare();
		handler = new Handler();
		Looper.loop();
		
	}

	
	
//	public void run() {
//
//		Looper.prepare();
//
//		handler = new Handler() {
//
//			public void handleMessage(Message msg) {
//				
//				interrupt();
//
//			//---------------------------------
//			// build application packet and 
//		    // pass it to the receiver thread
//			//---------------------------------
//				
//			msg.getData().getParcelable("replica");  
//			HoverID hoverID = null; // TODO: extract the elements from the bundle, mainly the ID
//			
//			BBAReplicaPacket m = new BBAReplicaPacket();
//			m.setHoverID(hoverID);
//			// ...
//			
//			Store.algo.recvReplicaPkt(m);
//
//			// metrics
//			// ...
//			
//			Log.v("ReplicaReceiver", "replica received!!!");
//			}
//		};
//		
//		Looper.loop();
//		
//	}

}
