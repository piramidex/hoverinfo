package ch.unige.cui.asg.android.hoverinfo.middleware.retrieval;

import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;

public class Subscription {

	//-------------------------------------
	// Subscription attributes
	//-------------------------------------
	
	private MobApplID subscriber;
	private String nameInterest;
	private String contentInterest;
	

	//-------------------------------------
	// Subscription constructors
	//-------------------------------------

	/**
	 * Creates a subscriber which is interested in any hoverinfo
	 * being available at the current location.
	 * @param subscriber
	 */
	public Subscription(MobApplID subscriber) {
		this.subscriber = subscriber;
		nameInterest = "anything";
		contentInterest = "anything";
	}

	
	//-------------------------------------
	// Subscription attributes getters
	//-------------------------------------
	
	public MobApplID getSubscriber() { return subscriber; }
	
	public String getNameInterest() { return nameInterest; }
	
	public String getContentInterest() { return contentInterest; }
	
}
