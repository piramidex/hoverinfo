package ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.ClosedByInterruptException;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.FrameResolver;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.PacketReceiver;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.Store;

public class UdpPacketReceiver extends PacketReceiver {
	
	private Store storageModule = null;

	private DatagramSocket socket = null;
	private byte[] buff = null;

	
	//---------------------------------
	// Constructors
	//---------------------------------

	public static UdpPacketReceiver instance = null;
	public static UdpPacketReceiver getInstance() {
		if (instance == null) instance = new UdpPacketReceiver();
		return instance;
	}
	private UdpPacketReceiver() {}

	
	//---------------------------------
	// Initialization
	//---------------------------------

	public void initialize() {
		
		try {
			
			socket = new DatagramSocket(UdpNicParameters.sendingPort);
			buff = new byte[UdpNicParameters.bufferMaxLength];
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		storageModule = Store.getInstance();
	}


	//---------------------------------
	// Packets reception
	//---------------------------------
	
	public void listen() {

		try {

				// Listen

				Log.v(getClass().getSimpleName(), "waiting for msg");
				DatagramPacket packet = new DatagramPacket(buff, buff.length);
				socket.receive(packet);

				// Packet received
				
				String remoteIP = packet.getAddress().getHostAddress();
				byte data = packet.getData()[0];
				Log.v(getClass().getSimpleName(), "msg rcvd "
						+ String.valueOf(data) + " from "
						+ packet.getAddress().getCanonicalHostName());

				// Check if own packet rcvd
				
				if (UdpNicParameters.localIP.equalsIgnoreCase(remoteIP)) {
					Log.v(getClass().getSimpleName(), "received my own packet");
					// continue;
				}

				// Deliver data to the appropriate module
	
				storageModule.resolver.resolve(buff);
				
				
		} catch (SocketException e) {
			// for DatagramSocket.constructor()
			e.printStackTrace();
//		} catch (ClosedByInterruptException e) {
//			interrupt();
		} catch (IOException e) {
			// for DataramSocket.receive()
			e.printStackTrace();
		}

	}
}
	