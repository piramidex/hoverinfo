package ch.unige.cui.asg.android.hoverinfo.middleware.retrieval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.clients.ClientsManager;


/**
 * This class models a subscription manager whose tasks are those of
 * creating new subscriptions or removing them when asked by the retrieval
 * module.
 * 
 * @author alfredo
 *
 */
public class SubscriptionsManager {
	
	

	private static SubscriptionsManager instance = null;
	
	public static SubscriptionsManager getInstance() {
		if (instance == null) instance = new SubscriptionsManager();
		return instance;
	}
	
	
	
	//-------------------------------------
	// List of subscriptions
	//-------------------------------------
	
	private HashMap<Integer, Subscription> subscriptions;
	private int nextSubsID;
	
	
	
	//-------------------------------------
	// Subscriptions manager constructors
	//-------------------------------------
	

	public SubscriptionsManager() {
		subscriptions = new HashMap<Integer, Subscription>();
		nextSubsID = 0;
	}
	
	
	
	
	//-------------------------------------
	// New subscription methods
	//-------------------------------------

	/**
	 * This method is called whenever a new subscription is to be
	 * managed. It returns an identifier of the subscription.
	 */
	public int registerNewSubscription(MobApplID mobApplID) {
		
		Integer subsID = new Integer(nextSubsID);
		Subscription subs = new Subscription(mobApplID);
		
		subscriptions.put(subsID, subs);
		nextSubsID = nextSubsID % Integer.MAX_VALUE;
		
		return subsID.intValue();
	}


	
	//-------------------------------------
	// Remove subscription methods
	//-------------------------------------

	
	/**
	 * Removes a subscription associated to a mobile application.
	 */
	public boolean removeSubscription(int subscriptionID) {
		if (subscriptions.remove(new Integer(subscriptionID)) == null) return false;
		return true;
	}

	
	
	//-------------------------------------
	// Various
	//-------------------------------------

	
	public Iterator<Subscription> iterator() {
		return subscriptions.values().iterator();
	}




	public void initialize() {
		// TODO Auto-generated method stub
		
	}





}
