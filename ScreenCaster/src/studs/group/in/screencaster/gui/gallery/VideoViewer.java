package studs.group.in.screencaster.gui.gallery;

import studs.group.in.screencaster.R;
import android.content.Context;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class VideoViewer extends LinearLayout {
	public Context context;
	public VideoView videoView;
	public LayoutParams params;
	public Uri uri = null;
	
	public VideoViewer(Context contextData) {
		super(contextData);
		context = contextData;
		init();
	}
	
	public VideoViewer(Context contextData, Uri uriVideo) {
		super(contextData);
		context = contextData;
		uri = uriVideo;
		init();
	}

	private void init() {
		//setBackgroundColor(getResources().getColor(R.color.light_blue));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
		params.setMargins(2,1,2,1);
		videoView = new VideoView(context);
		
		if(uri!=null)
			videoView.setVideoURI(uri);
		else
			videoView.setBackgroundResource(R.raw.image_missing);
		
		addView(videoView, params);
	}
}