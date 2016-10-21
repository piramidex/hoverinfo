/**
 * 
 */
package ch.unige.cui.asg.android.hoverinfo.middleware.storage;

import ch.unige.cui.asg.android.hoverinfo.middleware.MobApplID;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.BBAFrameResolver;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.FrameResolver;

/**
 * @author alfredo
 *
 */
public class Store {
	
	
	//-------------------------------------
	// Alogrithms
	//-------------------------------------

	enum STORAGE_ALGO_TYPES {
		BBA_ALGO_TYPE
	}

	public AlgoParams algoParams = null;
	public StorageAlgorithm algo = null;
	public FrameResolver resolver = null;
	

	
	//-------------------------------------
	// Threads
	//-------------------------------------
	
	public ReplicasReceiverThread recvThread = null;
	
	
	//-------------------------------------
	// Storage instance 
	//-------------------------------------

	private static Store instance = null;
	public static Store getInstance() {
		if (instance == null) instance = new Store();
		return instance;
	}
	private Store() { }

	
	/*=======================================*\
	 |                                       |
	 |       STORAGE INITIALIZATION          |
	 |                                       |
	\*====================================== */
	
	public void initialize() {
		
		// Choose storage algorithm
		
		STORAGE_ALGO_TYPES algoType = STORAGE_ALGO_TYPES.BBA_ALGO_TYPE;  // TODO choose algo from preferences
		
		switch (algoType) {
		
		case BBA_ALGO_TYPE:
			
			algoParams = BBAParameters.getInstance();
			algo = BBAStorage.getInstance();
			resolver = BBAFrameResolver.getInstance();
			break;
			
		default:
		
		}

		// Configure algo
		
		algoParams.configure();

		// Initialize submodules

		algo.initialize();
		resolver.initialize();
		
		// Create packets receiver thread

		recvThread = new ReplicasReceiverThread();
		recvThread.start();
	}
	
	
	/*=======================================*\
	 |                                       |
	 |          STORAGE INTERFACE            |
	 |                                       |
	\*====================================== */

	
	public void store(MobApplID mobApplID, String name, String content, double anchorRadius, long ttl) {
//		HoverID hoverID = null;  // TODO create hoverID from mobApplID, name, and nodeID
		algo.store(mobApplID, name, content, anchorRadius, ttl);
	}
	
}
