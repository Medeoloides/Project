package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

public class MyMap extends FragmentActivity {

	SupportMapFragment mapFragment;
	GoogleMap map;
	final String TAG = "myLogs";
	GPSTracker trckr;
	  
	private int mapTypeNor = GoogleMap.MAP_TYPE_NORMAL;
	private int mapTypeSat = GoogleMap.MAP_TYPE_SATELLITE;
	private int mapTypeTer = GoogleMap.MAP_TYPE_TERRAIN;
	private int mapTypeHyb = GoogleMap.MAP_TYPE_HYBRID;
	private int mapType = mapTypeNor;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_map);
		
		trckr = new GPSTracker(getApplicationContext());

	    mapFragment = (SupportMapFragment) getSupportFragmentManager()
	        .findFragmentById(R.id.myMap);
	    map = mapFragment.getMap();
	    if (map == null) {
	    	finish();
	    	return;
	    }
	    
	    User.getInstance().getmUser().put("latitude", trckr.getLatitude());
	    User.getInstance().getmUser().put("longitude", trckr.getLongitude());
	    User.getInstance().getmUser().saveInBackground();
	    init();
	  }

	  private void init() {
		  map.setMyLocationEnabled(true);
		  
		  map.addMarker(new MarkerOptions()
          .title("You")
          .snippet("..are here")
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
          .position(new LatLng(User.getInstance().getmUser().getDouble("latitude"), User.getInstance().getmUser().getDouble("longitude"))));
	  }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  menu.add(0, 1, 0, "Normal");
	      menu.add(0, 2, 0, "Satellite");
	      menu.add(0, 3, 0, "Terrain");
	      menu.add(0, 4, 0, "Hybrid"); 
	      menu.add(0, 5, 0, "Profile");
	      menu.add(0, 6, 0, "Calendar");
	      return super.onCreateOptionsMenu(menu);
	  }
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId())  {
		  case 3:
			  Toast.makeText(getApplicationContext(), "Terrain", Toast.LENGTH_LONG).show();
			  mapType = mapTypeTer;
			  break;
		  case 1:
			  Toast.makeText(getApplicationContext(), "Normal", Toast.LENGTH_LONG).show();
			  mapType = mapTypeNor;
			  break;
		  case 2:
			  Toast.makeText(getApplicationContext(), "Satellite", Toast.LENGTH_LONG).show();
			  mapType = mapTypeSat;
			  break;
		  case 4:
			  Toast.makeText(getApplicationContext(), "Hybrid", Toast.LENGTH_LONG).show();
			  mapType = mapTypeHyb;
			  break;
		  case 5:
			  Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
			  startActivity(new Intent(getApplicationContext(), Profile.class));
			  break;
		  case 6:
			  Toast.makeText(getApplicationContext(), "Calendar", Toast.LENGTH_LONG).show();
			  startActivity(new Intent(getApplicationContext(), MyCalendar.class));
			  break;
		  }
		  map.setMapType(mapType);
		  return super.onOptionsItemSelected(item);
	  }
	  
		@Override
		public void onBackPressed() {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			alertDialogBuilder.setTitle("Exit Application?");

			alertDialogBuilder.setMessage("Are you sure?");
			alertDialogBuilder.setIcon(R.drawable.ic_launcher);
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ParseUser.logOut();
					User.setInstance(null);

					finish();
				}
			});
			alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}
}
