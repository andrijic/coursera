package hr.andrijic.uslugehr;

import java.util.ArrayList;

import android.view.MotionEvent;

import com.google.android.gms.maps.model.Marker;

public interface IFragmentCallback {
	public void markerInfoClicked(Marker marker);
	public void updateUslugeResults(ArrayList<UslugaEntity> results);
	
}
