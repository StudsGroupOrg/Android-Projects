package studs.group.in.screencaster.gui;

import studs.group.in.screencaster.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter{
private Context context;
private Integer[] mThumbIds = {R.raw.camera,R.raw.color_blue,R.raw.dark_designer,
								R.raw.penelope,R.raw.screen_caster,R.raw.home};


	public GridViewImageAdapter(Context c) {
		context=c;
	}
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;  
	
		if (convertView == null) {
	            imageView = new ImageView(context);
	            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(5,5,5,5);
	        } else {
	            imageView = (ImageView) convertView;
	        }
			imageView.setImageResource(mThumbIds[position]);
	        return imageView;
    }

}
