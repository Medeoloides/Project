package com.example.project;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUp extends Activity {
	
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}
	
	public void cancel(View v) {
		finish();
	}
	
	public void signUp(View v) {
		EditText name = (EditText) findViewById(R.id.username);
		EditText email = (EditText) findViewById(R.id.email);
		EditText reemail = (EditText) findViewById(R.id.reemail);
		EditText password = (EditText) findViewById(R.id.password);
		EditText repassword = (EditText) findViewById(R.id.rePassword);
		
		if((email.getText().toString().equals("")) 
				|| (reemail.getText().toString().equals("")) 
				|| (name.getText().toString().equals("")) 
				|| (password.getText().toString().equals(""))  
				|| (repassword.getText().toString().equals("")))  {
			Toast.makeText(getApplicationContext(), "Please, fill all fields", Toast.LENGTH_LONG).show();
		}
		else if(email.getText().toString().compareTo(reemail.getText().toString()) != 0) {
			Toast.makeText(getApplicationContext(), "E-mails don't match", Toast.LENGTH_LONG).show();
			
			email.setText("");
			reemail.setText("");
		}
		else if(password.getText().toString().compareTo(repassword.getText().toString()) != 0) {
			Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
			
			password.setText("");
			repassword.setText("");
		}
		else {
			final ParseUser user = new ParseUser();
			user.setUsername(name.getText().toString());
			user.setPassword(password.getText().toString());
			user.setEmail(email.getText().toString());
			
			pd = new ProgressDialog(this);
			pd.setTitle("New Account");
			pd.setMessage("Saving..");
			pd.show();
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Source");
			query.whereEqualTo("objectId", "ZPAlbyRWwE");
			query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if (e == null) {
						for (final ParseObject parseObject : list) {
							ParseFile imageFile = (ParseFile) parseObject.get("photo");
							if (imageFile != null) {
								imageFile.getDataInBackground(new GetDataCallback() {
									public void done(byte[] data, ParseException e) {
										if (e == null) {
											final ParseFile imgFile = new ParseFile("noavatar.png", data);
											imgFile.saveInBackground(new SaveCallback() {
													
												@Override
												public void done(ParseException e) {
													if (e == null) {
														user.put("photo", imgFile);

														user.signUpInBackground(new SignUpCallback() {
																  
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
										else {
											Toast.makeText(getApplicationContext(), "Can't get image", Toast.LENGTH_LONG).show();
											System.out.println(e.getMessage());
										}
									}
								});
							}
						}
					}
				}
			});
		}
	}
}
