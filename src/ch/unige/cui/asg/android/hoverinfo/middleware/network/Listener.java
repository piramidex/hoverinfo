package ch.unige.cui.asg.android.hoverinfo.middleware.network;

import android.os.Handler;
import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Replica;

public abstract class Listener {
	
	public Handler handler;
	
	public abstract void recvReplica(Replica h);

}
