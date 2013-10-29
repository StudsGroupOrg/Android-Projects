package studs.group.in.screencaster.gui.gallery;

import java.io.File;

import studs.group.in.screencaster.essentials.Essential;
import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ImagePagerAdapter extends PagerAdapter {
	private static final String TAG = ImagePagerAdapter.class.getSimpleName(); 
	
	private Context context;
	private File imagesDir;
	private String[] list;
	private TextView tv_header;

	public static Uri[] ThumbsIds;

	public ImagePagerAdapter(Context imageContext,TextView txtView) {
		context = imageContext;
		tv_header = txtView;
		imagesDir = new File(Essential.DIR_IMAGE);
		
		if(imagesDir!=null)
			list = imagesDir.list();
		
		ThumbsIds = new Uri[list.length];
		
		for(int i=0;i<list.length;i++) {
			Log.d(TAG,"list["+i+"] = "+list[i]);
			ThumbsIds[i] = Uri.parse("file://"+imagesDir.getAbsolutePath()+"/"+list[i]);
		}
	}

	@Override
	public int getCount() {
		return ThumbsIds.length;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
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
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		if(0<=position && position<=list.length) {
			Log.d(TAG,"Deltd: "+list[position]);
			((ViewGroup)view).removeView((View)object);
		}
	}
	
	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		if(0<=position && position<=list.length) {
			Log.d(TAG,"Creat: "+list[position]);
			ImageViewer imgV = new ImageViewer(context,ThumbsIds[position]);
			((ViewGroup)view).addView(imgV);
			tv_header.setText(list[position]);
			Log.d(TAG,"Added: "+list[position]);
			return imgV;
		}
		return null;
	}
}
