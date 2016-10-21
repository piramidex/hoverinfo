/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.network;

import java.util.StringTokenizer;

import android.location.Location;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Area;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.CircularArea;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.HoverID;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Name;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Content;

/**
 * @author alfredo
 *
 */
public class BBAReplicaPacket extends Packet {
	
	private HoverID hoverID;
	private Name name;
	private Content value;
	private Area anchorArea;
	private long ttl;
	
	private Location srcLoc;
	
	private boolean populate;
	
	
	public BBAReplicaPacket() {
		hoverID = null;
		name = null;
		value = null;
		anchorArea = null;
		ttl = Long.MIN_VALUE;
		populate = false;
	}
	
	
	public void setHoverID(HoverID hoverID) {
		this.hoverID = hoverID;
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	public void setValue(Content value) {
		this.value = value;
	}
	
	public void setAnchorArea(Area anchorArea) {
		this.anchorArea = anchorArea;
	}
	
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	
	public void setPopulate() {
		this.populate = true;
	}

	public HoverID getHoverID() {
		return hoverID;
	}

	public Name getName() {
		return name;
	}

	public Content getValue() {
		return value;
	}

	public Area getAnchorArea() {
		return anchorArea;
	}

	public long getTtl() {
		return ttl;
	}

	private byte[] frame = null;


	@Override
	public byte[] getAsDataFrame() {
		
		// determine length
		
		int length = 0;
		
		
		// reserve space
		
		frame = new byte[length];
		
		
		// set metadata fields
		
		// - applID
		// - senderNodeID
		// - hop counter
		// - pos sender
		
		
		// set data
		
        String frameStr = (populate ? "[POPU|" : "[REPL|");
        frameStr += 
        	hoverID + "|" +
        	name + "|" +
        	value + "|" +
        	((CircularArea)anchorArea).getCenter().getLatitude() + "|" +
        	((CircularArea)anchorArea).getCenter().getLongitude() + "|" +
        	((CircularArea)anchorArea).getRadius() + "|" +
        	ttl + "]";

        frame = frameStr.getBytes();
        
		return frame;
	}


	public void setSrcLoc(Object currentLocation) {
		// TODO Auto-generated method stub
		
	}


	public boolean isPopulate() {
		return populate;
	}


	@Override
	public void buildPacketFromFrame(byte[] frame) {
		
		String frameStr = new String(frame);
		StringTokenizer parser = new StringTokenizer(frameStr, "[|]");
		
		String MSG = parser.nextToken();
		populate = (MSG == "POPU");
		hoverID = new HoverID(parser.nextToken());
		name = new Name(parser.nextToken());
		value = new Content(parser.nextToken());
		double lat = Double.parseDouble(parser.nextToken());
		double lon = Double.parseDouble(parser.nextToken());
		double rad = Double.parseDouble(parser.nextToken());
		anchorArea = new CircularArea(lat, lon, rad);
		ttl = Long.parseLong(parser.nextToken());
		
	}
	
}
