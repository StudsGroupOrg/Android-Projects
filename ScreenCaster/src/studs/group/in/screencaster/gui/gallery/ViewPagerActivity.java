package studs.group.in.screencaster.gui.gallery;

import studs.group.in.screencaster.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.TextView;

public class ViewPagerActivity extends Activity {
	private ImagePagerAdapter adapter;
	private TextView txtView;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		txtView = (TextView) findViewById(R.id.tv_header);
		adapter = new ImagePagerAdapter(this,txtView);
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}
}
