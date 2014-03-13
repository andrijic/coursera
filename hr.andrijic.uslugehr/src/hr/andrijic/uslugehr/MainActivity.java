package hr.andrijic.uslugehr;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentTransaction;
import android.inputmethodservice.Keyboard.Key;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, IFragmentCallback, LocationListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	LocationManager locationManager;
	int TABPOSITION_MAP = 0;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private static MyMapFragment myMapFragment;
	private static Location lastGoodLocation = null;	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String bestProvider = locationManager.getBestProvider(new Criteria(), true);
				
		locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//setContentView(R.layout.map);
				
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private IFragmentCallback fragmentCallback = null;
		
		public SectionsPagerAdapter(FragmentManager fm, IFragmentCallback fragmentCallback) {
			super(fm);
			this.fragmentCallback = fragmentCallback;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			
			if(position == TABPOSITION_MAP){
				myMapFragment = new MyMapFragment();
				myMapFragment.setFragmentListener(fragmentCallback);
				return myMapFragment;
			}else{
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				
				return fragment;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public static class MyMapFragment extends Fragment{
		private IFragmentCallback fragmentCallback = null;
		private SupportMapFragment fragment = null;
	
		
		public MyMapFragment() {
			super();
		}
		
		public void setFragmentListener(IFragmentCallback fragmentCallback){			
			this.fragmentCallback = fragmentCallback;
		}
		
		public void setLocation(Location location){
			
			//SupportMapFragment fragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
			GoogleMap map = fragment.getMap();
			
			if(location != null && map != null){
				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
						new CameraPosition(
								new LatLng(location.getLatitude(), location.getLongitude()),
								13,
								0,
								0
						)
				);
				map.moveCamera(cameraUpdate);
			}
		}
		
//		@Override
//		public void onDestroyView() {			
//			super.onDestroyView();
////			SupportMapFragment fragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
//			if(fragment != null){
//				getFragmentManager().beginTransaction().remove(fragment).commit();
//			}
//		}
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.map, container, false);
			
			FragmentManager fm = getChildFragmentManager();
			SupportMapFragment sf = (SupportMapFragment)fm.findFragmentById(R.id.mapPlaceholder);
			if(sf ==null){
				fragment = new SupportMapFragment(){
					@Override
					public void onActivityCreated(Bundle savedInstanceState) {						
						super.onActivityCreated(savedInstanceState);
						
						GoogleMap map = getMap();
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
						map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
							
							@Override
							public void onInfoWindowClick(Marker marker) {
								
								marker.hideInfoWindow();
								fragmentCallback.markerInfoClicked(marker);
							}
						});
						
						map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
							
							@Override
							public boolean onMyLocationButtonClick() {
								return false;
							}
						});
					}
				};				
				
				fm.beginTransaction().replace(R.id.mapPlaceholder, fragment).commit();
			}
			
			TextView locationText = ((TextView)rootView.findViewById(R.id.editText1));
			
			locationText.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {					
					((TextView)v).setText("");					
				}
			}); 
			
			locationText.setOnKeyListener(new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(KeyEvent.KEYCODE_ENTER == keyCode){
						TextView pom = (TextView)v;
						
						Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
						try {
							List<Address> addresses = geocoder.getFromLocationName(pom.getText().toString(), 1);
							Address address = addresses.get(0);
							if(address!=null){
								lastGoodLocation.setLongitude(address.getLongitude());
								lastGoodLocation.setLatitude(address.getLongitude());
								myMapFragment.setLocation(lastGoodLocation);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return true;
				}
			});
			
			return rootView;
		}
	}

	@Override
	public void markerInfoClicked(Marker marker) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG",marker.getTitle());
	}

	@Override
	public void onLocationChanged(Location location) {
		
		if(location != null && location.getAccuracy()<2000){
			lastGoodLocation = location;
			updateLocationTextForLastGoogLocation();
		}
	}
	
	public void updateLocationTextForLastGoogLocation(){
		myMapFragment.setLocation(lastGoodLocation);
		locationManager.removeUpdates(this);
		
		Geocoder geocoder = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(lastGoodLocation.getLatitude(), lastGoodLocation.getLongitude(), 1);
			
			if(addresses != null && addresses.size() > 0){
				Address pom = addresses.get(0);
									
				((TextView)findViewById(R.id.editText1)).setText(pom.getAddressLine(0)+" "+pom.getAddressLine(1)+" "+pom.getAddressLine(2));
			}else{
				((TextView)findViewById(R.id.editText1)).setText("Unknown address for Lat: "+lastGoodLocation.getLatitude()+
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
		Log.i("MOJTAG","1");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG","1");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i("MOJTAG","1");
	}

}
