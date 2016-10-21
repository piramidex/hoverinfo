package ch.unige.cui.asg.android.hoverinfo.middleware.retrieval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService;
import ch.unige.cui.asg.android.hoverinfo.middleware.IHoverinfoServiceCallback;
import ch.unige.cui.asg.android.hoverinfo.middleware.Logging;
import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.CircularArea;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Coord;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.HoverID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Replica;
import ch.unige.cui.asg.android.hoverinfo.middleware.geolocalisation.GeoLocalisation;

/**
 * This class implements the push-based retrieval mechanism
 * that should....
 * - constantely check for mreplicas matching subscription
 * - also listen to buffer changements to see if there's a matching
 * - notify the subscriber in case there's a matching
 * 
 * @author alfredo
 *
 */
public class PushBasedRetrieval extends BroadcastReceiver {

    public static final String TAG = "PushBasedRetrieval";

	private static final int ADD_MATCHING_REPLICA = 0;
	private static final int REMOVE_MATCHING_REPLICA = 1;

	//-------------------------------------
	// Subscriptions interface 
	//-------------------------------------
	
	public void addSubscription(MobApplID mobApplID) {
		
		HoverinfoService.subscriptionsManager.registerNewSubscription(mobApplID);

		
	}
	
	public void newHoverinfoInsertedIntoBuffer(Replica h) {
		
	}
	
	
	//-------------------------------------
	// Subscribers notification
	//-------------------------------------
	

	public void notifySubscriber(MobApplID mobApplID, Replica h, int event) {
		
		// Get callback interface
		
		IHoverinfoServiceCallback callback = HoverinfoService.clientsManager.getCallback(mobApplID);
		// TODO: deal the case when callback is null
		
		
		// Notify the client

		switch (event) {
		
		case ADD_MATCHING_REPLICA:

			String hoverID = h.getHoverID().getValue();
			String name = h.getName().getValue();
			String content = h.getContent().getValue();
			double latitude = ((CircularArea)h.getAnchorArea()).getCenter().getLatitude();
			double longitude = ((CircularArea)h.getAnchorArea()).getCenter().getLongitude();
			double radius = ((CircularArea)h.getAnchorArea()).getRadius();
			
			try {
				callback.insertHoverinfo(hoverID, name, content, latitude, longitude, radius);
				Logging.log(TAG, "deliver hoverinfo to app (hid=" + h.getHoverID() + ")");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
			
		case REMOVE_MATCHING_REPLICA:
			
			try {
				callback.removeHoverinfo(h.getHoverID().getValue());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		
		
	}
	
	
	
	
	//-------------------------------------
	// Matching                            
	//-------------------------------------
	
	
	/**
	 * This methods matches a subscription to the whole replicas stored
	 * in the buffer of the middleware. Currently, replicas are only 
	 * matched based on whether the node is inside the anchor area or not
	 * of a replica.
	 * @param subs
	 * @return
	 */
	public List<Replica> matchSubscription(Subscription subs) {

		List<Replica> matchingRepls = null;

		Location currentLoc = HoverinfoService.geoloc.getCurrentLocation();
		
		Iterator<Replica> it = HoverinfoService.buffer.iterator();
		for(; it.hasNext(); ) {
			Replica h = (Replica)it.next();
			
			boolean matching = false; 
			
			// match anchor area
			
			matching = h.getAnchorArea().containsLocation(currentLoc);
			
			// match name

//			matching = matching && h.getName().toString().equals(subs.getNameInterest());
			matching = matching && subs.getNameInterest().equals("anything");

			// match content
			
			matching = matching && subs.getContentInterest().equals("anything");
		
			if (matching) {
				matchingRepls.add(h);
			}
			
		}
		
		return matchingRepls;
	}

	

	/**
	 * This method returns all subscriptions matching the replicas
	 * passed as parameter.
	 * @param h
	 * @return
	 */
	public List<Subscription> matchReplica(Replica h) {
		
		Location currentLoc = HoverinfoService.geoloc.getCurrentLocation();

		List<Subscription> matchingSubs = new ArrayList<Subscription>();

		Iterator<Subscription> it = HoverinfoService.subscriptionsManager.iterator();
		for(; it.hasNext(); ) {
			Subscription subs = (Subscription)it.next();
			
			boolean matching = false; 
			
			// match anchor area
			
			matching = h.getAnchorArea().containsLocation(currentLoc);
			
			// match name

//			matching = matching && h.getName().toString().equals(subs.getNameInterest());
			matching = matching && subs.getNameInterest().equals("anything");

			// match content
			
			matching = matching && subs.getContentInterest().equals("anything");
		
			if (matching) {
				matchingSubs.add(subs);
			}
			
		}
		
		return matchingSubs;
	}
	
	
	
	//-------------------------------------
	// Buffer listener 
	//-------------------------------------
	
	
	/**
	 * This methods is called by the buffer whenever a new
	 * replica has been inserted into the buffer. So that
	 * the push-based retrieval module can check for matching
	 * and create proximity alerts.
	 */
	public void onNewReplicaInserted(Replica h) {
		
		// Match with current subscriptions and notify them
		
		List<Subscription> matchingSubs = matchReplica(h);
		
		Iterator<Subscription> it = matchingSubs.iterator();
		for(; it.hasNext(); ) {
			Subscription subs = (Subscription)it.next();
			notifySubscriber(subs.getSubscriber(), h, ADD_MATCHING_REPLICA);
		}
		
		// Add proximity alerts for the replica

		Intent intent = new Intent(HoverinfoService.class.getName());
		intent.putExtra("hoverID", h.getHoverID().getValue());
		
		int requestCode = 0; // TODO parameter not used???

		PendingIntent proxIntent = PendingIntent.getBroadcast(
				HoverinfoService.context,
				requestCode,
				intent,
				PendingIntent.FLAG_CANCEL_CURRENT);  // TODO is this the right flag?

		Coord center = ((CircularArea)h.getAnchorArea()).getCenter();
		double radius = ((CircularArea)h.getAnchorArea()).getRadius();
		
		HoverinfoService.locationManager.addProximityAlert(
				center.getLatitude(),
				center.getLongitude(),
				(float) radius,
				-1,
				proxIntent
				);
		
	}
	
	
	/**
	 * This method is called by the buffer whenever a replicas
	 * has been removed from the buffer. In this way, the push-based
	 * retrieval module can notify related subscribers of the event
	 * and remove proximity alerts.
	 * @param h
	 */
	public void onReplicaRemoved(Replica h) {

		// Match with current subscription and notify them
		
		List<Subscription> matchingSubs = matchReplica(h);
		
		Iterator<Subscription> it = matchingSubs.iterator();
		for(; it.hasNext(); ) {
			Subscription subs = (Subscription)it.next();
			notifySubscriber(subs.getSubscriber(), h, REMOVE_MATCHING_REPLICA);
		}
		
		// Remove proximity alerts
		
		//....

	}
	
	
	
	//-------------------------------------
	// Proximity alerts listener 
	//-------------------------------------

	
	/**
	 * THis methods is called whenever a proximity alert has been received
	 * by the middleware. The respective hoverID of the related replica is
	 * received as parameter, as well as if it is about an entering or not
	 * event. The method should trigger a matching process between the 
	 * subscriptions and the replica related to this alert.
	 */
	public void onProximityAlertReceived(HoverID hoverID, boolean entering) {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		boolean entering = intent.getBooleanExtra(key, false);

		int event = (entering ? ADD_MATCHING_REPLICA : REMOVE_MATCHING_REPLICA);

		HoverID hoverID = new HoverID(intent.getStringExtra("hoverID"));
		Replica h = HoverinfoService.buffer.getReplica(hoverID);

		List<Subscription> matchSubs = matchReplica(h);
		
		Iterator<Subscription> it = matchSubs.iterator();
		for (; it.hasNext(); ) {
			Subscription subs = it.next();
			MobApplID subsApplID = subs.getSubscriber();
			notifySubscriber(subsApplID, h, event);
		}

	}
}
