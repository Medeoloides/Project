package com.example.project;
import com.parse.Parse;

import android.app.Application;


public class MyApp extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "xE7rdqtP0L4cCJAfUID3zA1ZvIf4bplz2sysWLu0", "oWhxLyyAaPYfP6u0xZRrqMaO2QgywtHIHJ8IvuvC");
	}
}
