package ch.unige.cui.asg.android.hoverinfo.middleware.storage;


public class BBAParameters extends AlgoParams {
	
	public static long replTimer = -1;
	public static long REPLICATION_TIMER = -1;
	public static double PROB_FLOODING_POPULATE = -1;
	public static double MIN_FLOODING_POPULATE_DISTANCE_THRESHOLD = -1;

	
	//--------------------------------
	// Constructors
	//--------------------------------
	
	private BBAParameters() {}
	private static BBAParameters instance = null;
	public static BBAParameters getInstance() {
		if (instance == null) instance = new BBAParameters();
		return instance;
	}

	//--------------------------------
	// Configuration
	//--------------------------------

	@Override
	public void configure() {
		
		REPLICATION_TIMER = 10000;
		MIN_FLOODING_POPULATE_DISTANCE_THRESHOLD = 80;
		PROB_FLOODING_POPULATE = 0.6;
		
	}



	
	

}
