package ch.unige.cui.asg.android.hoverinfo.middleware.storage;
import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;

public abstract class StorageAlgorithm {
	
	
	public abstract void initialize();
	
	
	public abstract void store(
			MobApplID mobApplID,
			String name,
			String content,
			double anchorRadius,
			long ttl);
	
}