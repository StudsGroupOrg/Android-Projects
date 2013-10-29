package studs.group.in.screencaster.essentials;

import java.io.File;

import studs.group.in.screencaster.utils.execution.ExecutionManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Essential {
	/*Do not modify ever*/
	public static final int iApiLevel = android.os.Build.VERSION.SDK_INT;
	
	private static final String TAG = Essential.class.getSimpleName();
	/*Do not modify ever*/
	
	public static String  SDCard;
	public static boolean binStatus;
	
	public static String FBDEV;
	public static int	 frameWidth;
	public static int	 frameHeight;
	
	public static String IP_ADDR;
	public static int	 IP_CPORT;
	public static int	 IP_DPORT;
	public static int	 VIDEO_REC;
	
	public static String DIR_BIN;
	public static String DIR_HOME;
	public static String DIR_IMAGE;
	public static String DIR_VIDEO;
	
	private static Context appContext;
	private static WindowManager windowMan;
	private static Display display;
	//private static SensorManager sensorMan;
	
	public static void initialize(Context context) {
		int ipDecimal;

		appContext	= context;
		ipDecimal	= ((WifiManager)appContext
					.getSystemService(Context.WIFI_SERVICE))//Wifi Manager
					.getConnectionInfo()					//Wifi info
					.getIpAddress();						//Decimal ip(32-bit int)
		//sensorMan	= (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
		
		FBDEV		= "/dev/graphics/fb0";
		IP_ADDR		= decimalToIPV4(ipDecimal);
		IP_CPORT	= 50001;
		IP_DPORT	= 50000;
		VIDEO_REC	= 30;

		SDCard		= Environment.getExternalStorageDirectory().getAbsolutePath();
		DIR_BIN		= appContext.getFilesDir()+"/bin";
						/*  /data/data/<package_name>/files/bin  */
		DIR_HOME	= SDCard+"/ScreenCaster";
		DIR_IMAGE	= DIR_HOME+"/images";
		DIR_VIDEO	= DIR_HOME+"/videos";
		
		setFrameDimensions();
		createDirectories();
		isFFmpegInstalled();
	}

	private static void createDirectories() {
		// TODO Auto-generated method stub
		File temp;
		temp = new File(DIR_BIN);
		if(!temp.exists()) {
			if(temp.mkdirs()) {
				Log.i(TAG, temp.getAbsolutePath()+" created");
			}
			else {

				Log.e(TAG, temp.getAbsolutePath()+" failed");
			}
		}

		temp = new File(DIR_HOME);
		if(!temp.exists()) {
			if(temp.mkdirs()) {
				Log.i(TAG, temp.getAbsolutePath()+" created");
			}
			else {

				Log.e(TAG, temp.getAbsolutePath()+" failed");
			}
		}

		temp = new File(DIR_IMAGE);
		if(!temp.exists()) {
			if(temp.mkdirs()) {
				Log.i(TAG, temp.getAbsolutePath()+" created");
			}
			else {

				Log.e(TAG, temp.getAbsolutePath()+" failed");
			}
		}

		temp = new File(DIR_VIDEO);
		if(!temp.exists()) {
			if(temp.mkdirs()) {
				Log.i(TAG, temp.getAbsolutePath()+" created");
			}
			else {

				Log.e(TAG, temp.getAbsolutePath()+" failed");
			}
		}
	}

	private static String decimalToIPV4(int ipDecimal) {

	    int a=ipDecimal/(256*256*256);
	    int b=(ipDecimal%(256*256*256))/(256*256);
	    int c=((ipDecimal%(256*256*256))%(256*256))/256;
	    int d=((ipDecimal%(256*256*256))%(256*256))%256;
	    String ip = d+"."+c+"."+b+"."+a;
	    Log.d(TAG, "IPAddress : "+ipDecimal+" "+ip);
	    return ip;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings({ "null", "deprecation" })
	private static void setFrameDimensions() {
		// TODO Auto-generated method stub
		windowMan = (WindowManager)appContext.getSystemService(Context.WINDOW_SERVICE);
		display = windowMan.getDefaultDisplay();
		if(iApiLevel>=13) {
			Point size = null;
			display.getSize(size);
			frameWidth = size.x;
			frameHeight = size.y;
		} else {
			frameWidth = display.getWidth();
			frameHeight = display.getHeight();
		}
	}

	public static void grantFBAccess() {
		// TODO Auto-generated method stub
		try {
			Log.d(TAG,"Granting fb access exit="+
						ExecutionManager.execShellCommand("su",null,"chmod 777 "+Essential.FBDEV) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFFmpegInstalled() {
		// TODO Auto-generated method stub
		File temp = new File(DIR_BIN+"/ffmpeg");
		if(temp.exists()) {
			try {
				Log.d(TAG,"Changing ffmpeg perms exit="+
					ExecutionManager.execShellCommand("su",null,"chmod 777 "+temp.getAbsolutePath()) );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			binStatus=true;
		}
		else {
			Toast.makeText(appContext, "FFmpeg binary not installed!!", Toast.LENGTH_LONG).show();
			binStatus=false;
		}
		return binStatus;
	}
	
	public static void restrictFBAccess() {
		try {
			Log.d(TAG,"Restricting fb access exit="+
					ExecutionManager.execShellCommand("su",null,"chmod 660 "+Essential.FBDEV) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}