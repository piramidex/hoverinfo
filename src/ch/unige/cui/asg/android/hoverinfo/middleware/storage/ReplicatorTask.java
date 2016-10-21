/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.storage;

import java.util.TimerTask;

/**
 * @author alfredo
 *
 */
public class ReplicatorTask extends TimerTask {

	/**
	 * Check for each replica in the buffer to replicate.
	 */
	public void run() {
		Store.algo.replicate();
	}

}
