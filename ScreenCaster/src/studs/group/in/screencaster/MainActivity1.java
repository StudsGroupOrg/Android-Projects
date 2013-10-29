package studs.group.in.screencaster;

import studs.group.in.screencaster.gui.AppHeadService;
import studs.group.in.screencaster.gui.MTabsPagerAdapter;
import studs.group.in.screencaster.gui.NewGallaryView;
import studs.group.in.screencaster.gui.handleactionbartabs.ActionBarTabMain;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity1 extends FragmentActivity implements ActionBar.TabListener{

	AppHeadService app_head;
	private	ViewPager mViewPager;
	private MTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
    String tab_names[]={"Caster","Videos","Screenshot"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Initilization	
		mViewPager = (ViewPager) findViewById(R.id.main_pager);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        mAdapter = new MTabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
       
        // Adding Tabs
		for(int i=0;i<3;i++){
			Tab tab=actionBar.newTab();
			tab.setText(tab_names[i]);
			tab.setTabListener(this);
			actionBar.addTab(tab);
        }
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater=getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);	//setting Menu
		menuInflater.inflate(R.menu.main, menu);		//ActionBar Menu
		return true;
		}
//handle menu Click
	public void menuClicker(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu4_theme:
			changeTheme();
			Toast.makeText(this,"Theme Changed",Toast.LENGTH_SHORT).show();
			break;
		case R.id.munu6_exit:
			finish();
			break;
		case R.id.action_menu:
			Intent intLike=new Intent(getApplicationContext(),ActionBarTabMain.class);
			startActivity(intLike);
			break;

		case R.id.action_like:
			Intent intNew=new Intent(getApplicationContext(),NewGallaryView.class);
			startActivity(intNew);
			break;

		}
	}
	public void changeTheme(){
		getWindow().setBackgroundDrawableResource(R.raw.royal_blue);
	}
}
