/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.network;

import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Area;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.HoverID;

/**
 * @author alfredo
 *
 */
public abstract class Packet {

	public abstract byte[] getAsDataFrame();
	public abstract void buildPacketFromFrame(byte[] frame);

	
}
