package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

/**
 * This class models a replica of a hoverinfo. A replica is intended to be stored
 * in the buffer of replicas of a node. 
 * 
 * @author Alfredo Villalba (alfredo.villalba@unige.ch)
 *
 */
public class Replica {
	
	//---------------------------------
	// Hoverinfo attributes
	//---------------------------------
	
	private HoverID hoverID;
	private Name name;
	private Content content;
	private Area anchorArea;
	private long ttl;
	
	
	//---------------------------------
	// Constructors
	//---------------------------------
	
	public Replica() {
		hoverID = null;
		name = null;
		content = null;
		anchorArea = null;
		ttl = Long.MIN_VALUE;
	}
	
	
	public Replica(HoverID ID, Name name, Content content, Area anchorArea, long ttl) {
		this.hoverID = ID;
		this.name = name;
		this.content = content;
		this.anchorArea = anchorArea;
		this.ttl = ttl;
	}


	//---------------------------------
	// Setters and getters
	//---------------------------------

	public void setHoverID(HoverID hoverID) {
		this.hoverID = hoverID;
	}


	public void setName(Name name) {
		this.name = name;
	}

	
	public void setContent(Content value) {
		this.content = value;
	}

	
	public void setAnchorArea(Area anchorArea) {
		this.anchorArea = anchorArea;
	}

	
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	
	public HoverID getHoverID() {
		return hoverID;
	}

	
	public Name getName() {
		return name;
	}
	
	
	public Content getContent() {
		return content;
	}
	
	
	public Area getAnchorArea() {
		return anchorArea;
	}
	
	
	public long getTtl() {
		return ttl;
	}
}
