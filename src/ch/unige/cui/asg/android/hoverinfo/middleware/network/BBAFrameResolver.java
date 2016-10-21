package ch.unige.cui.asg.android.hoverinfo.middleware.network;

import java.util.StringTokenizer;

import android.util.Log;

import ch.unige.cui.asg.android.hoverinfo.middleware.Logging;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.BBAStorage;
import ch.unige.cui.asg.android.hoverinfo.middleware.storage.Store;

public class BBAFrameResolver extends FrameResolver {
	
	public static final String TAG = "BBAFrameResolver";
	
	public Store storageModule = null;
	
	//-----------------------------------
	// Constructors
	//-----------------------------------
	
	private BBAFrameResolver() { }
	private static BBAFrameResolver instance = null;
	public static BBAFrameResolver getInstance() {
		if (instance == null) instance = new BBAFrameResolver();
		return instance;
	}

	
	
	@Override
	public void initialize() {
		storageModule = Store.getInstance();
	}

	@Override
	public void resolve(byte[] frame) {
		
		String frameStr = new String(frame);
		StringTokenizer parser = new StringTokenizer(frameStr, "[|]");
		
		String MSG = parser.nextToken();
		
		if (MSG.equals("REPL")) {
			
			Log.v(TAG, "frame recognized as REPL");
			Logging.log(TAG, "frame recognized as REPL");
			
			final BBAReplicaPacket m = new BBAReplicaPacket();
			m.buildPacketFromFrame(frame);

			storageModule.recvThread.handler.postDelayed(
					new Runnable() {
						@Override
						public void run() {
							((BBAStorage)(storageModule.algo)).recvReplicaPkt(m);
						}
					}, 
					0);
		}
		
		else if (MSG.equals("POPU")) {

			Log.v(TAG, "frame recognized as POPU");
			Logging.log(TAG, "frame recognized as POPU");
	
			final BBAReplicaPacket m = new BBAReplicaPacket();
			m.buildPacketFromFrame(frame);

			storageModule.recvThread.handler.postDelayed(
					new Runnable() {
						@Override
						public void run() {
							((BBAStorage)(storageModule.algo)).recvPopulatePkt(m);
						}
					}, 
					0);

		}
	}


}
