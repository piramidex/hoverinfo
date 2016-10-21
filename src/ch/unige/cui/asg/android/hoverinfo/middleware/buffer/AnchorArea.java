/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

/**
 * @author alfredo
 *
 */
public abstract class AnchorArea {

	public abstract boolean isInside(Coord point);
	public abstract double distanceToPoint(Coord point);
}
