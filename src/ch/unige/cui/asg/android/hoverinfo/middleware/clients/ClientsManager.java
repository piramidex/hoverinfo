package ch.unige.cui.asg.android.hoverinfo.middleware.clients;

import java.util.HashMap;

import ch.unige.cui.asg.android.hoverinfo.middleware.IHoverinfoServiceCallback;
import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpPacketSender;

public class ClientsManager {

	private HashMap<MobApplID, IHoverinfoServiceCallback> callbacks;
	

	private static ClientsManager instance = null;
	
	public static ClientsManager getInstance() {
		if (instance == null) instance = new ClientsManager();
		return instance;
	}
	
	
	
	private ClientsManager() {
		callbacks = new HashMap<MobApplID, IHoverinfoServiceCallback>();
	}
	
	

	//-----------------------------------
	// Clients insertion
	//-----------------------------------
	
	public void insertClient() {}

	
	//-----------------------------------
	// Clients removal
	//-----------------------------------

	
	public void removeClient() {}
	
	
	//-----------------------------------
	// Access to clients
	//-----------------------------------

	public IHoverinfoServiceCallback getCallback(MobApplID mobApplID) {
		return callbacks.get(mobApplID);
	}
	
	
	//-----------------------------------
	// Callbacks management
	//-----------------------------------

	public void registerCallback(MobApplID mobApplID, IHoverinfoServiceCallback callback) {
		callbacks.put(mobApplID, callback);
	}



	public void initialize() {
		// TODO Auto-generated method stub
		
	}



}
