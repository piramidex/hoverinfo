package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

/**
 * 
 */

import android.location.Location;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Area;

/**
 * @author alfredo
 *
 */
public class Coord {
	
	private double x;
	private double y;
	
	public Coord(double lat, double lon) {
		x = lat;
		y = lon;
		
	}
	public Coord(Location loc) {
		x = loc.getLatitude();
		y = loc.getLongitude();
	}
	
	public double getLatitude() { return x; }
	public double getLongitude() { return y; }

	public double distanceTo(Coord point) {
		return Math.sqrt(((x - point.x) * (x - point.x)) + ((y - point.y) * (y - point.y)));
	}
	
	public boolean isInside(Area area) {
		return false;
	}
		
}
