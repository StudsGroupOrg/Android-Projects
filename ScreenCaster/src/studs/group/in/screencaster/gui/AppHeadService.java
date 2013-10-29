package studs.group.in.screencaster.gui;


import studs.group.in.screencaster.R;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class AppHeadService extends Service {
	private WindowManager windowManager;
	public ImageView appHead;
	WindowManager.LayoutParams params;
	public AppHeadService() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
		appHead=new ImageView(this);
		appHead.setImageResource(R.raw.camera);
		
		params=new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, 
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

//Set AppHead location
		params.gravity=Gravity.TOP|Gravity.RIGHT;
		params.x=0;
		params.y=100;
		windowManager.addView(appHead, params);

//Set AppHead movement	
		
		appHead.setOnTouchListener(new OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX + (int) (event.getRawX() - initialTouchX);
					params.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(appHead, params);
					return true;
				}
				return false;
			}
		}); 
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	public void removeHead(){
		if (appHead != null) windowManager.removeView(appHead);
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	
}
