package com.example.project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
 
public class Profile extends Activity {
	
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		TextView name = (TextView) findViewById(R.id.textView1);
		name.setText(User.getInstance().getmUser().getUsername().toString());
		
		final ImageButton photo = (ImageButton) findViewById(R.id.imageButton1);
		
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
										}
									}
								});
							}
					}
				}
		}});
	
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), Photo.class));
			}
		});
		
		photo.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				final Dialog imageDialog = new Dialog(getApplicationContext());
				imageDialog.setContentView(R.layout.image_upload);
				imageDialog.setTitle("Download Profile Photo");
				
				Button browseGallery = (Button) imageDialog.findViewById(R.id.browseGallery);
				browseGallery.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
					}
				});
				return false;
			}
		});
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                
                final ImageButton photo = (ImageButton) findViewById(R.id.imageButton1);
                
                pd = new ProgressDialog(this);
    			pd.setTitle("New Photo");
    			pd.setMessage("Saving..");
    			pd.show();
    			
                photo.setImageURI(selectedImageUri);
                final ParseUser user = User.getInstance().getmUser();
                
                final File imgFile = new  File(selectedImagePath);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] img = stream.toByteArray();
                final ParseFile imageFile = new ParseFile("avatar.png", img);
                imageFile.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							user.put("photo", imgFile);

							user.saveInBackground(new SaveCallback() {
									  
								@Override
								public void done(ParseException e) {
									pd.cancel();

									if (e == null) {
										User.getInstance().setmUser(user);
										startActivity(new Intent(getApplicationContext(), MyMap.class));
									}		 
									else {
										Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
										System.out.println(e.getMessage());
									}
								}
							});
						}
						else {
							Toast.makeText(getApplicationContext(), "Image Failed", Toast.LENGTH_SHORT).show();
							System.out.println(e.getMessage());
						}
					}              	
                });          
            }
        }
    }
    
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
}


