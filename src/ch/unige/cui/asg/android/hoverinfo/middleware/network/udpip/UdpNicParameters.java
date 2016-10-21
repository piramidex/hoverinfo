package ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip;

import ch.unige.cui.asg.android.hoverinfo.middleware.network.NicParameters;

public class UdpNicParameters extends NicParameters {

	//--------------------------------
	// NIC parameters
	//--------------------------------
	
	public static String localIP = null;
	public static String bcastAddress = null;
	public static int sendingPort = -1;
	public static int bufferMaxLength = -1;

	
	//--------------------------------
	// Constructors
	//--------------------------------
	

	public static UdpNicParameters instance = null;
	public static UdpNicParameters getInstance() {
		if (instance == null) instance = new UdpNicParameters();
		return instance;
	}
	
	private UdpNicParameters() {}

	
	//--------------------------------
	// Configuration
	//--------------------------------
	
	
	@Override
	public void configure() {
		
		// TODO get configuration values from preferences
		
		localIP = "192.168.1.2";
		bcastAddress = "192.168.1.255";
		sendingPort = 9999;
		bufferMaxLength = 256;
	}
}
