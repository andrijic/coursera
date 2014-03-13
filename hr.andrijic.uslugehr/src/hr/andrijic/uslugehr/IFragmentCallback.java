package hr.andrijic.uslugehr;

import android.view.MotionEvent;

import com.google.android.gms.maps.model.Marker;

public interface IFragmentCallback {
	public void markerInfoClicked(Marker marker);
	
}
