package ch.unige.cui.asg.android.hoverinfo.middleware;

public class MobApplID {
	
	private String mobApplID;
	
	public MobApplID(String mobApplID) {
		this.mobApplID = mobApplID;
	}
	
	public String getValue() {
		return mobApplID;
	}
	
	public boolean equals(Object o) {
		
		MobApplID id = (MobApplID)o;
		if (mobApplID.equalsIgnoreCase(id.mobApplID)) return true;
		return false;
		
	}
	
	public int hashCode() {
		return mobApplID.hashCode();
	}
	
	public String toString() { return mobApplID; }

}
