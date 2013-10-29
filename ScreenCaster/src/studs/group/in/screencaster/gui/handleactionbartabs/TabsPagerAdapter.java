package studs.group.in.screencaster.gui.handleactionbartabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	/** page is requested to create */
	@Override
	public Fragment getItem(int index) {
		Fragment fragment;
		switch(index){
		case 0:
			fragment= new FragmentScreenShots();
			fragment.setArguments(null);
			return fragment;
		case 1:
			fragment= new FragmentVideos();
			fragment.setArguments(null);
			return fragment;
			
		case 2:
			fragment=new FragmentCaster();
			fragment.setArguments(null);
			return fragment;
			
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
