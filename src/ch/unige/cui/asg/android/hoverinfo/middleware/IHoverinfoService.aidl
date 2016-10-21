package ch.unige.cui.asg.android.hoverinfo.middleware;

import ch.unige.cui.asg.android.hoverinfo.middleware.IHoverinfoServiceCallback;

interface IHoverinfoService {

	void store(String mobApplID, String name, String content, double radius, long ttl);
	String retrieve(String mobApplID, String name);
	void subscribe(String mobApplID, double radiusOfInterest);
	void registerCallback(String mobApplID, IHoverinfoServiceCallback callback);
}
