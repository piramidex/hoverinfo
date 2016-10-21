package ch.unige.cui.asg.android.hoverinfo.middleware.buffer;

import java.util.Collection;
import java.util.HashMap;

import android.util.Log;

public class BufferV1 extends Buffer {

	long maxSize;
	HashMap<HoverID, Replica> replicas;

	//--------------------------------------
	// Constructors
	//--------------------------------------
	
	public BufferV1(long maxSize) {
		this.maxSize = maxSize;
		this.replicas = new HashMap<HoverID, Replica>((int) maxSize);
	}

	
	
	//--------------------------------------
	// Access methods
	//--------------------------------------
	

	@Override
	public Replica get(HoverID ID) {
		Replica h = replicas.get(ID);
		return h;
	}
	
	public Collection<Replica> iterator() {
		return replicas.values();
	}
	
	
	//--------------------------------------
	// Insertion methods
	//--------------------------------------

	@Override
	public Replica insert(HoverID hoverID, String name, String value, Area anchorArea, long ttl) {
		if (replicas.get(hoverID) != null) {
			Log.e("buffer", "trying to insert an alredy inserted replica - this shouldn't hapeen");
			// TODO: return an exception???
			return null;
		}
		if (replicas.size() > maxSize) return null;

		Replica h = new ReplicaV1(hoverID, new Name(name), new Value(value), anchorArea, ttl);
		replicas.put(hoverID, h);
		return h;
	}

	
	
	//--------------------------------------
	// Disposal methods
	//--------------------------------------
	

	@Override
	public void remove(String mobApplID, String name) {
	}


	@Override
	public void insert(Replica h) {
		Name key = h.name;
		replicas.put(key, h);
	}


	@Override
	public int size() {
		return replicas.size();
	}

}
