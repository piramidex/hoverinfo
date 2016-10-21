package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.NodeID;

/**
 * This class models a hoverinfo identifier that will be used by the replicas.
 * The current implement models an identifier as a simple string.
 * 
 * @author Alfredo Villalba (alfredo.villalba@unige.ch)
 *
 */
public class HoverID {

	private String value;  // the string value of the identifier
	
	//-------------------------------
	// Constructors and factories
	//-------------------------------
	
	/**
	 * This method generates a new hover identifier which is a combinaison
	 * of the node identifier, the mobile application identifier, the name 
	 * of the hoverinfo and the current system time. In this way, we can
	 * guarantee the uniqueness of hoverinfo identifiers.
	 * 
	 * @param nodeID node identifier
	 * @param mobApplID mobile application identifier
	 * @param name hoverinfo name
	 * @return hoverinfo identifier
	 */
	public static HoverID genHoverID(NodeID nodeID, MobApplID mobApplID, String name) {
		String value = nodeID.getValue() + "-" + mobApplID.getValue() + name + System.currentTimeMillis();
		return new HoverID(value);
		
	}

	public HoverID(String hoverID) {
		this.value = hoverID;
	}

	
	//-------------------------------
	// Setters and getters 
	//-------------------------------

	public String getValue() {
		return value;
	}


	//-------------------------------
	// Comparaison
	//-------------------------------

	/**
	 * This method compares two hoverinfo identifers.
	 * They are considered as equals if their string value
	 * is the same.
	 */
	public boolean equals(Object o) {
		HoverID hoverID2 = (HoverID)o;
		return value.equals(hoverID2.value);
	}
	
	/**
	 * The hash code is used by the hash maps? or maps?
	 * Anyway this method return a hash code which is the
	 * same hash code of the string value of the identifier.
	 */
	public int hashCode() {
		return value.hashCode();
	}

	
	//-------------------------------
	// Miscellaneous
	//-------------------------------

	public String toString() { return value; }

}
