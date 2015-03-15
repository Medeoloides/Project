package com.example.project;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MyCalendar extends Activity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
	
	public void onClick (View v){
    	Intent intent1 = new Intent ();
		
		EditText et1 = (EditText)findViewById(R.id.att);
		intent1.putExtra("locationatt", et1.getText().toString());
		EditText et2 = (EditText)findViewById(R.id.longit);
		intent1.putExtra("locationlongit", et1.getText().toString());
		
    	
    	Calendar cal = Calendar.getInstance();              
    	Intent intent = new Intent(Intent.ACTION_EDIT);
    	intent.setType("vnd.android.cursor.item/event");
    	intent.putExtra("beginTime", cal.getTimeInMillis());
    	intent.putExtra("allDay", false);
    	intent.putExtra("rrule", "FREQ=DAILY");
    	intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
    	intent.putExtra("title", et1 + " " + et2);
    	SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    	String formattedDate = df.format(cal.getTime());
    	intent.putExtra("date", formattedDate);
    	startActivity(intent);
    	finish();
    }
}
