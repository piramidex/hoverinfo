package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import java.util.Iterator;
import java.util.TimerTask;

import android.util.Log;
import ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService;

public class CleaningTask extends TimerTask {

	private static final String TAG = "CleaningTask";

	@Override
	public void run() {
		
		Log.v(TAG, "periodical check for cleaning");
		
		// Remove replicas
		
		Iterator<Replica> it = HoverinfoService.buffer.iterator();
		for(; it.hasNext();) {
			Replica h = it.next();
			
			if (HoverinfoService.geoloc.distanceTo(h.getAnchorArea()) > HoverinfoService.CLEANING_DISTANCE) {
				HoverinfoService.buffer.remove(h.getHoverID());
			}
		}

		// Cancel thread if buffer empty
		
		if (HoverinfoService.buffer.size() == 0) {
			Buffer.cleanThread.cancel();
		}
	}
}
