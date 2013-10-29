package studs.group.in.screencaster.gui.handleactionbartabs;
import studs.group.in.screencaster.R;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public class ActionBarTabMain extends FragmentActivity implements ActionBar.TabListener {
		private	ViewPager viewPager;
		private TabsPagerAdapter mAdapter;
		private ActionBar actionBar;
	    
	    String tab_names[]={"Screenshot","Videos","Caster"};
	    
	    @Override	
	    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar_tab);

		// Initilization	
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
//        actionBar.setHomeButtonEnabled(false);
       
        // Adding Tabs
		for(int i=0;i<3;i++){
			Tab tab=actionBar.newTab();
			tab.setText(tab_names[i]);
			tab.setTabListener(this);
			actionBar.addTab(tab);
        }
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_bar_tab, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	

}
