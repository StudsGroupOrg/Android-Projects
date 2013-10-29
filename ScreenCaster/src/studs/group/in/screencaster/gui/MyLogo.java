package studs.group.in.screencaster.gui;

import studs.group.in.screencaster.MainActivity2;
import studs.group.in.screencaster.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class MyLogo extends Activity {
	private static final String TAG = MyLogo.class.getSimpleName(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_logo);
		
		new CountDownTimer(3000,1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				int iApiLevel = android.os.Build.VERSION.SDK_INT;
				Log.i(TAG, "ApiLevel = "+iApiLevel);
				if(8<=iApiLevel && iApiLevel<=11) {
					Intent in1 = new Intent(getApplicationContext(),
							MainActivity2.class);
					startActivity(in1);
				}
				if(11<=iApiLevel && iApiLevel<=18){
					Intent in2 = new Intent(getApplicationContext(),
							MyLogo.class);
					startActivity(in2);
				}
				finish();
			}
		}.start();
	}
}
