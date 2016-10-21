package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import android.location.Location;

public abstract class Area {
	
	public abstract boolean containsLocation(Location location); 
	public abstract double distanceToLocation(Location location);

}
