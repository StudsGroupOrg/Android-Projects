package studs.group.in.screencaster.essentials;

import java.util.Timer;
import java.util.TimerTask;

import studs.group.in.screencaster.device.Native;
import studs.group.in.screencaster.utils.execution.ExecutionManager;
import studs.group.in.screencaster.utils.network.NetworkManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service /*implements SensorEventListener*/ {
	private static final String TAG	= BackgroundService.class.getSimpleName();
	
	private final IBinder mBinder = new ServiceBinder();
	
	private boolean operation = false;
	
	private ExecutionManager execMan;
	private NetworkManager ntwkMan;
	private Thread execThread;
	private Timer timer;
	
	/*public Sensor mProximity;
	public Sensor mAccelerometer;*/

	public BackgroundService() throws Exception {
		
		/*mProximity = Essential.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    mAccelerometer = Essential.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    
	    Essential.sensorManager.registerListener(this,mProximity,
	    		SensorManager.SENSOR_DELAY_FASTEST);
	    Essential.sensorManager.registerListener(this,mAccelerometer,
    			SensorManager.SENSOR_DELAY_FASTEST);*/

	    execMan = new ExecutionManager();
	    Log.d(TAG,"Constructed");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG,"Bound");
	  	return mBinder;
	}
	   
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG,"Start commanded");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG,"Destroying service");
		cleanUp();
		Log.d(TAG,"Destroyed");
		super.onDestroy();
	}
	
	public class ServiceBinder extends Binder {
		public BackgroundService getService() {
			return BackgroundService.this;
   		}
	}

	/*@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}*/

	/*@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_PROXIMITY:		
			proximityEvent(event.values[0]);
			break;
		case Sensor.TYPE_ACCELEROMETER:
			accelerometerEvent(event.values);
			break;
		}
	}*/
	
	/*private void accelerometerEvent(float[] accelOf) {
		// TODO Auto-generated method stub
		final int iXaxis=0,iYaxis=1,iZaxis=2;
		
		if(accelOf[iZaxis]>19)
			Log.i(TAG,"ACCEL: "+accelOf[iXaxis]+" "+
										accelOf[iYaxis]+" "+
										accelOf[iZaxis]);
	}*/

	/*private void proximityEvent(float proxim) {
		// TODO Auto-generated method stub
		//int exit=0;
		
		//frameDir = getFilesDir();
		if(proxim == 0) {
			Log.d(TAG,"Near: "+proxim);
			try {
				//exit = Native.haveRoot();
				//Native.captureScreen(1);
				//String rawFrame = Essential.DEF_DIR_IMAGE+"/raw_frame";
				//String newJpeg = Essential.DEF_DIR_IMAGE+"/Snap_"+System.currentTimeMillis()+".jpg";
				//String capCommand = "cat "+Essential.DEF_FBDEV+" > "+rawFrame+" \n"+
						//"ffmpeg -vcodec rawvideo -f rawvideo -pix_fmt rgb32 -s "+Essential.frameWidth+"x"+Essential.frameHeight+" -i "+rawFrame+
								//" -f image2 -q:v 2 -vcodec mjpeg "+newJpeg+" 2> /dev/null \n"+
						//"rm -f "+rawFrame+" \n";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"PROXIM: Exception => "+e);
			}
		}
		else
			Log.d(TAG,"Faar: "+proxim);
	}*/
	
	//Check for any running threads
	private boolean svcIsOperating() {
		// TODO Auto-generated method stub
		Log.d(TAG, "1="+svcIsCasting()+" 2="+svcIsRecording()+" 3="+operation);
		return (svcIsCasting() || svcIsRecording() || operation);
	}
	
	//Screenshot methods
	public void svcScreenshot() throws Exception {
		if(!Essential.isFFmpegInstalled()) {
			Log.e(TAG,"FFmpeg binary not found");
			return;
		}
		Essential.grantFBAccess();
		String rawFrame = Essential.DIR_IMAGE+"/raw_frame";
		String newJpeg = Essential.DIR_IMAGE+"/Snap_"+System.currentTimeMillis()+".jpg";
		//String capCommand = "cat "+Values.FBDEV+" > "+rawFrame+" \n"+
		String capCommand = "dd if="+Essential.FBDEV+" bs="+(Essential.frameHeight*4)+" count="+Essential.frameWidth+" "
				+"skip="+(Essential.frameWidth*Native.getCurrent(Essential.FBDEV))+" of="+rawFrame+" \n"+
				Essential.DIR_BIN+"/ffmpeg -vcodec rawvideo -f rawvideo -pix_fmt rgb32 -s "+Essential.frameWidth+"x"+Essential.frameHeight+" -i "+rawFrame+
						" -f image2 -q:v 2 -vcodec mjpeg "+newJpeg+" 2> /dev/null \n"+
				"rm -f "+rawFrame+" \n";
		ExecutionManager.execShellCommand("su",null,capCommand);
		Essential.restrictFBAccess();
		Log.d(TAG,"Cap command = "+capCommand);
		Log.d(TAG,"Screenshot "+newJpeg+" saved");
	}
	//Screenshot methods~
	
	//Screen record methods
	public void svcSetRecordingParameters() {
		
	}
	
	public void svcStartRecording() throws Exception {
		if(!svcIsOperating()) {
			if(!Essential.isFFmpegInstalled()) {
				Log.e(TAG,"FFmpeg binary not found");
				return;
			}
			Essential.grantFBAccess();
			String newVid = Essential.DIR_VIDEO+"/Vid_"+System.currentTimeMillis()+".flv";
			execMan.setArguments("su",null,
					Essential.DIR_BIN+"/ffmpeg -f fbdev -i "+Essential.FBDEV+" -r 15 "+newVid);
			execThread = new Thread(execMan);
			execThread.start();
			timer = new Timer();
			startTimer();
			Log.d(TAG,"Screen record "+newVid+" started");
		} else {
			Log.d(TAG,"Rec: Some operation active");
		}
	}

	public void svcStopRecording() throws Exception {
		if(svcIsRecording()) {
			ExecutionManager.destroyForcibily("q\n");
			Log.d(TAG,"Recording stopped");
			Essential.restrictFBAccess();
		} else {
			Log.d(TAG,"Not recording");
		}
	}
	
	public boolean svcIsRecording() {
		return ExecutionManager.isActive();
	}
	//Screen record methods~
	
	//Screen cast methods
	public void svcCastVideoRotation(double angleDegree) {
		if(ntwkMan!=null) {
			ntwkMan.setRotation(angleDegree);
		}
	}
	
	public void svcCastVideoZoom(double zoomFactor) {
		if(ntwkMan!=null) {
			ntwkMan.setZoomLevel(zoomFactor);
		}
	}
	
	public void svcStartCasting() {
		if(!svcIsOperating()) {
			//ntwkThread = new Thread(ntwkHandler);
			ntwkMan = null;
			ntwkMan = new NetworkManager();
			ntwkMan.startCast();
			timer = new Timer();
			startTimer();
			Log.d(TAG,"Screencast started");
		} else {
			Log.d(TAG,"Cast: Some operation active");
		}
	}
	
	public void svcStopCasting() {
		if(svcIsCasting()) {
			ntwkMan.stopCast();
			Log.d(TAG,"Screencast stopped");
		} else {
			Log.d(TAG,"Not casting");
		}
	}
	
	public boolean svcIsCasting() {
		return ntwkMan!=null && ntwkMan.isActive();
	}
	//Screen cast methods~

	//Cleans up before service is destroyed
	public void cleanUp() {
		// TODO Auto-generated method stub
		try {
			svcStopCasting();
			svcStopRecording();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"Exception "+e.getMessage());
		}
		Essential.restrictFBAccess();
		stopTimer();
		//Essential.sensorManager.unregisterListener(this);
		Log.d(TAG, "CleanUp complete");
	}
	
	//Timer starter
	private void startTimer() {
		// TODO Auto-generated method stub
		operation = true;
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
			    operation = false;
			  }
		}, 1000);
	}
	
	//Timer stopper
	private void stopTimer() {
		// TODO Auto-generated method stub
		timer.cancel();
	}
}