package ch.unige.cui.asg.android.hoverinfo.middleware.network;

public abstract class FrameResolver {
	
	public abstract void resolve(byte[] frame);

	public abstract void initialize();

}
