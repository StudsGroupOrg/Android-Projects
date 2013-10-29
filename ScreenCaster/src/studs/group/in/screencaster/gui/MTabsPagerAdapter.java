package studs.group.in.screencaster.gui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MTabsPagerAdapter extends FragmentPagerAdapter {

	public MTabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	/** page is requested to create */
	@Override
	public Fragment getItem(int index) {
		Fragment mfragment;
		switch(index){
		case 0:
			mfragment=new MFragmentCaster();
			mfragment.setArguments(null);
			return mfragment;
		case 1:
			mfragment= new MFragmentVideos();
			mfragment.setArguments(null);
			return mfragment;
			
		case 2:
			mfragment= new MFragmentScreenShots();
			mfragment.setArguments(null);
			return mfragment;

			
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
