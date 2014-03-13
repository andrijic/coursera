package hr.andrijic.uslugehr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
	private String MOJTAG = "MOJTAG";
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private Marker currentPositionMarker;
		
	private Location lastGoodLocation = null;
	
	public MyMapFragment() {
		super();
	}
	
	public void setFragmentListener(IFragmentCallback fragmentCallback){			
		this.fragmentCallback = fragmentCallback;
	}
	
	public void setLocation(Location location){
		
		//SupportMapFragment fragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
		Log.i(MOJTAG,"setLocation : "+location.getLongitude()+", "+location.getLatitude());
		
		if(location != null && map != null){
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
				MarkerOptions options = new MarkerOptions()
												.position(new LatLng(location.getLatitude(), location.getLongitude()))
												.title("Hello world");
				options.icon(descriptor);
				
				currentPositionMarker = map.addMarker(options);
			}
		}
	}
	
	@Override
	public void onPause() {	
		super.onPause();
		Log.i(MOJTAG,"onPause");
		
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		Log.i(MOJTAG,"onResume"); 
		
		String bestProvider = locationManager.getBestProvider(new Criteria(), true);
				
		locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.i(MOJTAG,"onCreate");
		
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
		Log.i(MOJTAG,"onCreateView");
		
		View rootView = inflater.inflate(R.layout.map, container, false);
		
		TextView locationText = ((TextView)rootView.findViewById(R.id.editText1));
		
		locationText.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {					
				((TextView)v).setText("");					
			}
		}); 
		
		
		
		return rootView;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.i("MOJTAG","onLocationChanged");
		
		if(location != null && location.getAccuracy()<2000){
			lastGoodLocation = location;
			setLocation(lastGoodLocation);
			Log.i("MOJTAG","lastGoodLocation set");
			
			updateLocationTextForLastGoogLocation();
			
			locationManager.removeUpdates(this);
		}
	}
	
	public void updateLocationTextForLastGoogLocation(){
		
		Geocoder geocoder = new Geocoder(getActivity());
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(lastGoodLocation.getLatitude(), lastGoodLocation.getLongitude(), 1);
			
			if(addresses != null && addresses.size() > 0){
				Address pom = addresses.get(0);
									
				((TextView)getView().findViewById(R.id.editText1)).setText(pom.getAddressLine(0)+" "+pom.getAddressLine(1)+" "+pom.getAddressLine(2));
			}else{
				((TextView)getView().findViewById(R.id.editText1)).setText(R.string.UNKOWN_ADDRESS+" Lat: "+lastGoodLocation.getLatitude()+
						" Long: "+lastGoodLocation.getLongitude());
			}
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


}