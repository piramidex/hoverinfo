/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.network.udpip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ch.unige.cui.asg.android.hoverinfo.middleware.network.Packet;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.PacketSender;

import android.util.Log;



/**
 * @author alfredo
 *
 */
public class UdpPacketSender extends PacketSender {
	
	
	DatagramSocket senderSocket = null;

	
	//---------------------------------
	// Constructors
	//---------------------------------

	public static UdpPacketSender instance = null;
	public static UdpPacketSender getInstance() {
		if (instance == null) instance = new UdpPacketSender();
		return instance;
	}
	
	
	//---------------------------------
	// Initialization
	//---------------------------------
	
	@Override
	public void initialize() {
		try {
			senderSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//---------------------------------
	// Sending methods
	//---------------------------------
	
	@Override
	public void bcast(Packet m) {
		
		try {

			// Build frame
			
			byte[] buff;
			buff = m.getAsDataFrame();
			
			DatagramPacket datagram;
			datagram = new DatagramPacket(
					buff,
					buff.length,
					InetAddress.getByName(UdpNicParameters.bcastAddress),
					UdpNicParameters.sendingPort
					);
			
			// Broadcast frame
			
			senderSocket.setBroadcast(true);
			senderSocket.send(datagram);
			
			Log.v(getClass().getSimpleName(), "frame sent");
			
			//----[metrics]
			//numSentMsgs++;
			//---
			
		} catch (UnknownHostException e) {
			// for InetAddress.getByName()
			e.printStackTrace();
		} catch (IOException e) {
			// for DatagramSocket.send()
			e.printStackTrace();
		}
	
	}
}
