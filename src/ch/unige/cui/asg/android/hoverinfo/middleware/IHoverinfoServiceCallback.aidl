package ch.unige.cui.asg.android.hoverinfo.middleware;

interface IHoverinfoServiceCallback {

	void insertHoverinfo(String hoverID, String name, String value, double latitude, double longitude, double radius);
	void removeHoverinfo(String hoverID);

}
