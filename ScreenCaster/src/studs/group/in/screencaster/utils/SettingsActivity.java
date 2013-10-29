package studs.group.in.screencaster.utils;

import studs.group.in.screencaster.R;
import studs.group.in.screencaster.essentials.BackgroundService;
import studs.group.in.screencaster.essentials.Essential;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private static final String TAG = SettingsActivity.class.getSimpleName();
	
	public BackgroundService mBgService;
	
	private boolean mIsBound = false;
	private Intent inBind;
	
	private int count = 0;
	private double angleDegree = 90;
	private double zoomFactor = 100;

	private ServiceConnection sCon = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder binder) {
			mBgService = ((BackgroundService.ServiceBinder)binder).getService();
			Log.d(TAG,"Scon");
		}

		public void onServiceDisconnected(ComponentName name) {
			mBgService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}
	
	private void initService() {
		doBindService();
		startService(inBind);
	}
	
	private void doBindService() {
		if(!mIsBound) {
			inBind=new Intent(this,BackgroundService.class);
			bindService(inBind,sCon,Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}
	
	private void doUnbindService() {
		if(mIsBound) {
			unbindService(sCon);
			stopService(inBind);
			mIsBound = false;
		}
	}
	
	public void settingsClicker(View v) {
		try {
			switch(v.getId()) {
			case R.id.btn_start_serv:
				initService();
				if(mBgService!=null && mBgService.svcIsCasting()) {
					mBgService.svcCastVideoRotation(count*angleDegree);
					mBgService.svcCastVideoZoom(zoomFactor/100);
				}
				break;
			case R.id.btn_stop_serv:
				mBgService.cleanUp();
				doUnbindService();
				break;
			case R.id.btn_scr_start_record:
				mBgService.svcStartRecording();
				break;
			case R.id.btn_scr_stop_record:
				mBgService.svcStopRecording();
				break;
			case R.id.btn_scr_start_cast:
				Toast.makeText(getApplicationContext(), 
						"IP is "+Essential.IP_ADDR, Toast.LENGTH_LONG).show();
				mBgService.svcStartCasting();
				break;
			case R.id.btn_scr_stop_cast:
				mBgService.svcStopCasting();
				break;
			case R.id.btn_scr_shot:
				mBgService.svcScreenshot();
				break;
			case R.id.btn_scr_rotate_cast:
				count++;
				count%=4;
				mBgService.svcCastVideoRotation(count*angleDegree);
				break;
			case R.id.btn_scr_zoomin_cast:
				zoomFactor+=50;
				zoomFactor%=400;
				mBgService.svcCastVideoZoom(zoomFactor/100);
				break;
			case R.id.btn_scr_zoomout_cast:
				zoomFactor-=50;
				zoomFactor%=400;
				if(zoomFactor<=10)
					zoomFactor=10;
				mBgService.svcCastVideoZoom(zoomFactor/100);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
