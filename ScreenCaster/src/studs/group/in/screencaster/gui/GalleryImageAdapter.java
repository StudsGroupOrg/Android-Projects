package studs.group.in.screencaster.gui;

import java.util.List;

import studs.group.in.screencaster.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GalleryImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private List<String> myFiles;
	
	public GalleryImageAdapter(Context c,List<String> newImgList) {
		mContext=c;
		myFiles=newImgList;
		
		TypedArray a=c.obtainStyledAttributes(R.styleable.Gallery1);
		mGalleryItemBackground=a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground,0);
		a.recycle();
	
	}

	@Override
	public int getCount() {
		return myFiles.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView i=new ImageView(mContext);
		Bitmap bm=BitmapFactory.decodeFile(myFiles.get(position).toString());
		i.setImageBitmap(bm);
		
		i.setLayoutParams(new Gallery.LayoutParams(150,150));
		i.setScaleType(ImageView.ScaleType.FIT_XY);
	    i.setBackgroundResource(mGalleryItemBackground);

	    return i;
	}
	

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return myFiles.size();
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
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
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
