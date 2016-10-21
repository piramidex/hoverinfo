package ch.unige.cui.asg.android.hoverinfo.middleware.geolocalisation;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Area;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.CircularArea;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Coord;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.Store;

// TODO: make HIM robust to the absence of GPS signal


public class GeoLocalisation implements LocationListener {
	
	private Location lastLocation;
	private boolean providerAvailable;
	
	private static GeoLocalisation instance = null;

	public static GeoLocalisation getInstance() {
		if (instance == null) instance = new GeoLocalisation();
		return instance;
	}
	
	public void initialize() {
		lastLocation = null;
		HoverinfoService.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, HoverinfoService.LOCATION_UPDATES_INTERVAL, 0, this); // TODO: manage when the gps provider is not available
		lastLocation = HoverinfoService.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  // TODO lastlocation might null!!!
		
	}
	
	public double distanceTo(Area anchorArea) {
		Coord loc = new Coord(lastLocation);
		double dist = loc.distanceTo(((CircularArea)anchorArea).getCenter());
		return dist;
	}



	public boolean isNodeInside(Area anchorArea) {
		Coord loc = new Coord(lastLocation);
		double dist = loc.distanceTo(((CircularArea)anchorArea).getCenter());
		if (dist <= ((CircularArea)anchorArea).getRadius()) return true;
		return false;
	}

	public Location getCurrentLocation() {
		return lastLocation; // TODO do something smarter when the location provider is unavailable
	}

	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		providerAvailable = false;
	}

	@Override
	public void onProviderEnabled(String provider) {
		providerAvailable = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
			
			switch (status) {
			
			case LocationProvider.OUT_OF_SERVICE:
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				providerAvailable = false;
				break;
				
			case LocationProvider.AVAILABLE:
				providerAvailable = true;
			}
		}
	}


}
