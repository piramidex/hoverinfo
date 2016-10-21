/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

/**
 * This class models the name of a hoverinfo. In the current implementation,
 * the name is simply modeled as a string.
 * 
 * @author Alfredo Villalba (alfredo.villalba@unige.ch)
 */
public class Name {

	String name;  // the string value of the name
	
	public Name(String name) {
		this.name = name;
	}
	
	public String getValue() { return name; }
	
	public String toString() { return name; }

}
