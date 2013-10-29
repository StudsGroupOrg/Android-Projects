package studs.group.in.screencaster;

import studs.group.in.screencaster.essentials.Essential;
import studs.group.in.screencaster.gui.gallery.ViewPagerActivity;
import studs.group.in.screencaster.utils.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity2 extends Activity {
	public ImageView imgGallery;
	public ImageView imgSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		imgGallery = (ImageView) findViewById(R.id.imageView_gallery);
		imgSettings = (ImageView) findViewById(R.id.imageView_settings);
		
		Essential.initialize(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Intent in2 = new Intent(getApplicationContext(),
								SettingsActivity.class);
		startActivity(in2);
		return true;
	}

	public void customClickListener(View v) {
		switch(v.getId()) {
		case R.id.imageView_gallery:
			Intent in1 = new Intent(getApplicationContext(),
									ViewPagerActivity.class);
			startActivity(in1);
			break;
		case R.id.imageView_settings:
			Intent in2 = new Intent(getApplicationContext(),
			 						SettingsActivity.class);
			startActivity(in2);
			break;
		}
	}
}