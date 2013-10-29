package studs.group.in.screencaster.gui.gallery;

import studs.group.in.screencaster.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageViewer extends LinearLayout {
	private static final String TAG = ImageViewer.class.getSimpleName();
	
	private Bitmap bm;
	private Context context;
	private ImageView imageView;
	private LayoutParams params;
	private String pathName;
	private Uri uri = null;
	
	public ImageViewer(Context contextData) {
		super(contextData);
		context = contextData;
		init();
	}
	
	public ImageViewer(Context contextData, Uri uriImage) {
		super(contextData);
		context = contextData;
		uri = uriImage;
		init();
	}

	private void init() {
		//setBackgroundColor(getResources().getColor(R.color.light_blue));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
		params.setMargins(2,1,2,1);
		imageView = new ImageView(context);
		pathName = uri.toString();
		bm = BitmapFactory.decodeFile(pathName.substring(7,pathName.length()));
		Log.d(TAG,"BM_PATH: "+pathName.substring(7,pathName.length()));
		
		if(uri!=null)
			imageView.setImageBitmap(bm);
		else
			imageView.setBackgroundResource(R.raw.image_missing);
		
		addView(imageView, params);
	}
}