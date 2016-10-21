/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import android.location.Location;

/**
 * @author alfredo
 *
 */
public class CircularArea extends Area {
	
	private Coord center;
	private double radius;

	public CircularArea(Location center, double radius) {
		Coord loc = new Coord(center.getLatitude(), center.getLongitude());
		this.center = loc;
		this.radius = radius;
	}
	
	public CircularArea(double lat, double lon, double rad) {
		center = new Coord(lat, lon);
		radius = rad;
	}

	@Override
	public boolean containsLocation(Location location) {
		Coord loc = new Coord(location.getLatitude(), location.getLongitude());
		double dist = center.distanceTo(loc);
		if (dist <= radius) return true;
		return false;
	}

	@Override
	public double distanceToLocation(Location location) {
		Coord loc = new Coord(location.getLatitude(), location.getLongitude());
		return center.distanceTo(loc);
	}
	
	public Coord getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public String toString() {
	    String s = "";
	    s = "(lat=" + center.getLatitude() + ", lon=" + center.getLongitude() + ", r=" + radius + ")";
	    return s;
	}
}
