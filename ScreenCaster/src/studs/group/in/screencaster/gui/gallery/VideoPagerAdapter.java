package studs.group.in.screencaster.gui.gallery;

import java.io.File;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class VideoPagerAdapter extends PagerAdapter {
	private static final String TAG = VideoPagerAdapter.class.getSimpleName();
	
	private Context context;
	private File sdcard;
	private File videosDir;
	private String[] list;

	public static Uri[] ThumbsIds;

	public VideoPagerAdapter(Context imageContext) {
		context = imageContext;
		sdcard = Environment.getExternalStorageDirectory();
		videosDir = new File(sdcard.getAbsolutePath()+"/Videos/Test");
		
		if(videosDir!=null)
			list = videosDir.list();
		
		ThumbsIds = new Uri[list.length];
		for(int i=0;i<list.length;i++) {
			Log.d(TAG,"list["+i+"] = "+list[i]);
			ThumbsIds[i] = Uri.parse("file://"+videosDir.getAbsolutePath()+"/"+list[i]);
		}
	}

	@Override
	public int getCount() {
		return ThumbsIds.length;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (View)object;
	}

	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		((ViewGroup)view).removeView((View)object);
	}
	
	@Override
	public Object instantiateItem(View view, int position) {
		VideoViewer vidV = new VideoViewer(context,ThumbsIds[position]);
		((ViewPager)view).addView(vidV);
		return vidV;
	}
}
