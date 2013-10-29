package studs.group.in.screencaster.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import studs.group.in.screencaster.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;


public class NewGallaryView extends Activity {
	List<String> imgList= new ArrayList<String>();
	ImageView img_view;
	@SuppressWarnings("deprecation")
	Gallery gallery;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_gallary_view);
		
		gallery=(Gallery) findViewById(R.id.gallery1);
		img_view=(ImageView)findViewById(R.id.imageView1);
//		gallery.setSpacing(1);
		gallery.setAdapter(new GalleryImageAdapter(this,ReadSdCard()));
		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			// on itam click
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				//handle click
				
			}
		});
}
	private List<String> ReadSdCard() {
//		List<String> imgList= new ArrayList<String>();
		File path=new File("/mnt/sdcard/ScreenCaster/Screenshots/");//Environment.getExternalStorageDirectory().getPath()
		File[] files=path.listFiles();
		
		for(int i=0;i<files.length;i++){
			File img=files[i];
			imgList.add(img.getPath());
		}
		return imgList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_gallary_view, menu);
		return true;
	}
	
}
