/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.retrieval;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Replica;

/**
 * @author alfredo
 *
 */
public class QueryReceiverThread extends Thread {

	public Handler handler = null;
	
	public void run() {
		Looper.prepare();

		handler = new Handler() {
			public void handleMessage(Message msg) {
			// extract replica from msg
			
			Replica h;
			msg.getData().getParcelable("replica");
			
			
			// check replica properties
			//if (!HoverinfoService.buffer.contains(h);
			
			
			// send it to the buffer to store it
			
			Log.v("Query Receiver", "query received!!!");
			}
		};

		Looper.loop();
	}
}
