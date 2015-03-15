package com.example.project;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Photo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		final ImageView photo = (ImageView) findViewById(R.id.photo);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId", User.getInstance().getmUser().getObjectId());
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> list, ParseException e) {
				if (e == null) {
					for (final ParseUser parseUser : list) {
						ParseFile imageFile = (ParseFile) parseUser.get("photo");
						if (imageFile != null) {
							imageFile.getDataInBackground(new GetDataCallback() {
								public void done(byte[] data, ParseException e) {
									if (e == null) {
										Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
										photo.setImageBitmap(bmp);
									} 
									else {
										Toast.makeText(getApplicationContext(), "Can't to get image", Toast.LENGTH_LONG).show();
										System.out.println(e.getMessage());
									}
								}
							});
						}
					}
				}
		}});	
	}
}
