package ch.unige.cui.asg.android.hoverinfo.middleware.network;

public abstract class PacketSender {
	
	public abstract void initialize();
	public abstract void bcast(Packet m);
}