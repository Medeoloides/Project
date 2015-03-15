package com.example.project;

import com.parse.ParseUser;

public class User {

	public static User instance = null;
	private ParseUser mUser;
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	private User() {	
	}

	public ParseUser getmUser() {
		return mUser;
	}

	public void setmUser(ParseUser mUser) {
		this.mUser = mUser;
	}

	public static void setInstance(User instance) {
		User.instance = instance;
	}
}
