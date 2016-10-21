package ch.unige.cui.asg.android.hoverinfo.middleware;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.text.GetChars;
import android.text.format.DateFormat;

import android.util.Log;


//HIM-LOGGING
//
//Objective
//=======
//A logging module for HIM that will log any relevant event in order to measure the storage usage, the network messages sent, the battery usage, the gps locations, the mobile application requests...
//
//The ultimate goal would be that of:
//
//- measuring the survivability, availability and accessibility of all hoverinfos
//- measuring the network overhead
//- measuring the memory usage
//- measuring the success of retrieval requests
//- measuring the battery usage
//- measuring the gps traces
//
//How to measure the survability/availability/accessibility?
//===========================================
//
//- keep gps trace of all replicas (at each mobile node)
//- estimate the availability once all data collected
//
//How to measure retrieval performances?
//================================
//
//push-based
//
//- keep trace of all subscriptions
//- keep when data is delivered to mobile application
//- collect all data and estimate success
//
//pull-based
//
//- keep trace of all retrieval queries
//- keep trace of all answers
//- keep trace of delays, path, messages
//- collect of data and estimate global success

public class Logging {

	private static Context context;
	private static DataOutputStream fileLog;
	private static java.text.DateFormat dateFormat;
	private static java.text.SimpleDateFormat simpleDateFormat;
	
	public static void initialize(Context c) {
		context = c;
		dateFormat = DateFormat.getDateFormat(context);
		//dateFormat = DateFormat.getTimeFormat(context);
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		fileLog = openFileToWrite("him.log", true, true);
	}
	

	public static void log(String TAG, String data) {
		//String timestamp = dateFormat.format(System.currentTimeMillis());
        //String timestamp = String.valueOf(System.currentTimeMillis());
		//String timestamp = dateFormat.format(new Date());
	    String timestamp  = simpleDateFormat.format(System.currentTimeMillis());
		try {
			fileLog.writeUTF("[" + TAG + " - " + timestamp + "] " + data + "\n");
			fileLog.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	private static DataOutputStream openFileToWrite(String fileName, boolean append,
			boolean sdcard) {
		DataOutputStream dos = null;
		try {
			FileOutputStream fos;
			if (!sdcard)
				fos = context.openFileOutput(fileName, (append ? Context.MODE_APPEND
						: Context.MODE_PRIVATE));
			else {
				File sdroot = Environment.getExternalStorageDirectory();
				if (!sdroot.canWrite())
					Log.i(context.getClass().getSimpleName(),
							"Can't write into sd card!!!");
				File sdfile = new File(sdroot, fileName);
				fos = new FileOutputStream(sdfile, append);
			}
			dos = new DataOutputStream(new BufferedOutputStream(fos));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return dos;
	}

	private static DataInputStream openFileToRead(String fileName, boolean sdcard) {
		DataInputStream dis = null;
		try {
			FileInputStream fis;
			if (!sdcard)
				fis = context.openFileInput(fileName);
			else {
				File sdroot = Environment.getExternalStorageDirectory();
				File sdfile = new File(sdroot, fileName);
				fis = new FileInputStream(sdfile);
			}
			dis = new DataInputStream(new BufferedInputStream(fis));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return dis;
	}
	
}
