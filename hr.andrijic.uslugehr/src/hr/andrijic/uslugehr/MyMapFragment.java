package hr.andrijic.uslugehr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.input.InputManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends Fragment implements LocationListener{
	private IFragmentCallback fragmentCallback = null;	
	private LocationManager locationManager;
	private GoogleMap map;
	
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private Marker currentPositionMarker;
		
	private Location lastGoodLocation = null;
	private Location lastGoodMyLocation = null;
	
	public MyMapFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public void setFragmentListener(IFragmentCallback fragmentCallback){			
		this.fragmentCallback = fragmentCallback;
	}
	
	public void setLocation(Location location){
		
		//SupportMapFragment fragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
		
		
		if(location != null && map != null){
			Log.i(MainActivity.MOJTAG,"setLocation : "+location.getLongitude()+", "+location.getLatitude());
			
			CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
					new CameraPosition(
							new LatLng(location.getLatitude(), location.getLongitude()),
							12,
							0,
							0
					)
			);
			map.animateCamera(cameraUpdate);	
			
			
			if(currentPositionMarker == null){
				BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_indicator_current_position);
				MarkerOptions options = new MarkerOptions().title("Hello world");
				options.icon(descriptor);
				options.position(new LatLng(location.getLatitude(), location.getLongitude()));
				
				currentPositionMarker = map.addMarker(options);
			}
			
			currentPositionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
			
			((TextView)getView().findViewById(R.id.editText1)).setEnabled(false);
		}
	}
	
	@Override
	public void onPause() {	
		super.onPause();
		Log.i(MainActivity.MOJTAG,"onPause");
		
		locationManager.removeUpdates(this);
	}
	
	public void startTrackingCurrentLocation(){
		String bestProvider = locationManager.getBestProvider(new Criteria(), true);
		
		locationManager.requestLocationUpdates(bestProvider, 300, 0, this);
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		Log.i(MainActivity.MOJTAG,"onResume"); 
		startTrackingCurrentLocation();
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.i(MainActivity.MOJTAG,"onCreate");
		
		setRetainInstance(true);
		
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		FragmentManager fm = getChildFragmentManager();
		SupportMapFragment sf = (SupportMapFragment)fm.findFragmentById(R.id.mapPlaceholder);
		if(sf ==null){
			Fragment fragment = new SupportMapFragment(){
				@Override
				public void onActivityCreated(Bundle savedInstanceState) {						
					super.onActivityCreated(savedInstanceState);
					
					map = getMap();
					//map.setMyLocationEnabled(true);
					if(lastGoodLocation != null){
						CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
								new CameraPosition(
										new LatLng(lastGoodLocation.getLatitude(), lastGoodLocation.getLongitude()),
										13,
										0,
										0
								)
						);
						map.moveCamera(cameraUpdate);
					}
					
					Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Hello world"));
					markers.add(marker);
					
					map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
						
						@Override
						public void onInfoWindowClick(Marker marker) {
							
							marker.hideInfoWindow();
							fragmentCallback.markerInfoClicked(marker);
						}
					});		
					
					map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
						
						@Override
						public void onMapLongClick(LatLng point) {
							Log.i(MainActivity.MOJTAG, "Long press on map, changing location");
							Location location = new Location("");
							location.setLatitude(point.latitude);
							location.setLongitude(point.longitude);
							
							lastGoodLocation = location;
							setLocation(lastGoodLocation);
							updateLocationTextForLastGoogLocation();							
						}
					});
				}
			};				
			
			fm.beginTransaction().replace(R.id.mapPlaceholder, fragment).commit();
			
		}
	}
	
//	@Override
//	public void onDestroyView() {			
//		super.onDestroyView();
////		SupportMapFragment fragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
//		if(fragment != null){
//			getFragmentManager().beginTransaction().remove(fragment).commit();
//		}
//	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(MainActivity.MOJTAG,"onCreateView");
		
		View rootView = inflater.inflate(R.layout.map, container, false);
		
		final AutoCompleteTextView locationText = ((AutoCompleteTextView)rootView.findViewById(R.id.editText1));
		
		locationText.setEnabled(false);
		locationText.setText(R.string.LOOKING_LOCATION);
		
		locationText.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {					
				((AutoCompleteTextView)v).setText("");					
			}
		}); 
		
		locationText.setThreshold(5);	
		final AutocompleteArrayAdapter adapter = new AutocompleteArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item);
		locationText.setAdapter(adapter);
		locationText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Address address = adapter.getArrayList().get(arg2);
				Location location = new Location("reversegeocoded");
				location.setLatitude(address.getLatitude());
				location.setLongitude(address.getLongitude());
				setLocation(location);	
								
				locationText.setEnabled(false);
			}
		});
		
		ImageButton changeAddressButton = (ImageButton)rootView.findViewById(R.id.imageButton_changeAddress);
		changeAddressButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				locationText.setEnabled(true);
				locationText.setText("");
				
				InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				
				
			}
		});		
		
		ImageButton currentLocationButton = (ImageButton)rootView.findViewById(R.id.imageButton_currentLocation);
		currentLocationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setLocation(lastGoodMyLocation); //imediate return to the last meassured calculation
				startTrackingCurrentLocation();	//get better reading with better accurracy - fine tunning
				
				
			}
		});
		
		ImageButton searchButton = (ImageButton)rootView.findViewById(R.id.imageButton_search);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = getActivity().getString(R.string.uslugehrurl);
				new SearchServicesAsyncTask().execute(url);
				
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.i("MOJTAG","onLocationChanged");
		
		lastGoodLocation = location;
		setLocation(lastGoodLocation);
		Log.i("MOJTAG","lastGoodLocation set");
		
		updateLocationTextForLastGoogLocation();
		
		if(location != null && location.getAccuracy()<2000){			
			Log.i("MOJTAG","removeUpdates");
			locationManager.removeUpdates(this);
			lastGoodMyLocation = lastGoodLocation;
		}
	}
	
	public void updateLocationTextForLastGoogLocation(){
		
		Geocoder geocoder = new Geocoder(getActivity());
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(lastGoodLocation.getLatitude(), lastGoodLocation.getLongitude(), 1);
			
			AutoCompleteTextView locationText = ((AutoCompleteTextView)getView().findViewById(R.id.editText1));
			
			
			
			if(addresses != null && addresses.size() > 0){
				Address pom = addresses.get(0);
									
				String shortened = (pom.getAddressLine(0)+" "+pom.getAddressLine(1)+" "+pom.getAddressLine(2));
				locationText.setText(shortened);
			}else{
				locationText.setText(R.string.UNKOWN_ADDRESS+" Lat: "+lastGoodLocation.getLatitude()+
						" Long: "+lastGoodLocation.getLongitude());
			}
			
			locationText.dismissDropDown();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG","onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG","onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG","onStatusChanged");
	}

	public static class SearchServicesAsyncTask extends AsyncTask<String, Void, ArrayList<UslugaEntity>>{

		@Override
		protected ArrayList<UslugaEntity> doInBackground(String... params) {
			HttpURLConnection connection = null;
			ArrayList<UslugaEntity> list = new ArrayList<UslugaEntity>();
			
			try{
				URL url = new URL(params[0]);
				
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.setReadTimeout(10000);
				connection.connect();
				InputStream stream = connection.getInputStream();
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				while(stream.read(buffer) != -1){
					bstream.write(buffer);
				}
				
				String jsonResult = new String(bstream.toByteArray());
				
				
				JSONObject array = new JSONObject(jsonResult);
				Iterator<String> keys = array.keys();
				while(keys.hasNext()){					
					UslugaEntity usluga = new UslugaEntity(array.getJSONObject(keys.next()));
					list.add(usluga);
				}				
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(connection != null){
					connection.disconnect();
				}
			}
			
			return list;
		}
	
		@Override
		protected void onPostExecute(ArrayList<UslugaEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	
	}

}
