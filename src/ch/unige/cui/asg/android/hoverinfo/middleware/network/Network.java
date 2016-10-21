/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.network;

import ch.unige.cui.asg.android.hoverinfo.middleware.Measurable;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpNicParameters;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpPacketReceiver;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip.UdpPacketSender;
import android.widget.TextView;


/**
 * @author alfredo
 *
 */
public class Network implements Measurable {
	
	enum NETWORK_TYPES {
		UDP_NETWORK_TYPE
	}

	public NicParameters nicParams;
	public PacketSender pktSender;
	public PacketReceiver pktReceiver;
	
	public static Thread pktRecvThread = null;

	//--------------------------------
	// Constructors
	//--------------------------------
	

	public static Network instance = null;
	public static Network getInstance() {
		if (instance == null) instance = new Network();
		return instance;
	}
	
	private Network() {}
	
	//--------------------------------
	// Module initialization
	//--------------------------------

	public void initialize() {
		
		// Choose network and initialize
		
		NETWORK_TYPES netType = NETWORK_TYPES.UDP_NETWORK_TYPE;  // TODO choose network from preferences
		
		switch (netType) {
		
		case UDP_NETWORK_TYPE:
			
			nicParams = UdpNicParameters.getInstance();
			pktSender = UdpPacketSender.getInstance();
			pktReceiver = UdpPacketReceiver.getInstance();
			
		default:
		
		}

		// Configure NIC
		
		nicParams.configure();

		// Initialize submodules

		pktSender.initialize();
		pktReceiver.initialize();
		
		// Create receiver thread

		pktRecvThread = new PacketReceiverThread();
		pktRecvThread.start();
		
	}
	
	
	//--------------------------------
	// Module interface
	//--------------------------------
	
	public void bcast(Packet m) { pktSender.bcast(m); }
	
	

	/*======================================*\
	 |                                      |
	 |            METRICS                   |
	 |                                      |
	\*======================================*/

	protected long numSentMsgs;
	protected long numRcvdMsgs;
	
	private String[] metrics =
	{
			"sent messages",
			"rcvd messages"
	};

	public String[] getMetricsList() { return metrics; } 

	public double getMetricValue(int index) {
		
		switch (index) {
		
		case 0: return (double)numSentMsgs;
		case 1: return (double)numRcvdMsgs;
		
		}
		
		throw new IllegalArgumentException("metric index outside range");
	}
	
	

	/*======================================*\
	 |                                      |
	 |        RECEPTION THREAD              |
	 |                                      |
	\*======================================*/

	
	public class PacketReceiverThread extends Thread {
		
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				pktReceiver.listen();
			}
		}
	}
}
