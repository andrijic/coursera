package hr.andrijic.uslugehr;

import java.util.ArrayList;


import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class UslugeResultListFragment extends ListFragment implements IUslugeUpdateListener{

	private IFragmentCallback mFragmentCallback;
	
	public void setFragmentListener(IFragmentCallback mfFragmentCallback) {
		this.mFragmentCallback = mfFragmentCallback;
	}
	
	@Override
	public void notifyUpdateUslugeResults(ArrayList<UslugaEntity> results) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		return inflater.inflate(R.layout.result_list, container, false);
	}
}
