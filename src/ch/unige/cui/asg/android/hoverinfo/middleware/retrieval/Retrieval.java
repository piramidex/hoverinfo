/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.retrieval;

import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Name;

/**
 * @author alfredo
 *
 */
public class Retrieval {
	
	
	public static PushBasedRetrieval pushBasedRetrieval = new PushBasedRetrieval();
	
	public void retrieve(String mobApplID, Name name) {
	}
	
	
	public void subscribe(String mobApplID, double radiusOfInterest) {
		pushBasedRetrieval.addSubscription(new MobApplID(mobApplID));
	}
	
}
