package hr.andrijic.uslugehr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


public class AutocompleteArrayAdapter extends ArrayAdapter<String> implements Filterable{
	
	ArrayList<Address> resultList = new ArrayList<Address>();
	
	public AutocompleteArrayAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getItem(int position) {
		return addressToString(resultList.get(position));
	}
	
	@Override
	public int getCount() {
		return resultList.size();
	}
	
	public String addressToString(Address address){
		StringBuffer buffer = new StringBuffer();
		
		if(address!=null){
			if(address.getAddressLine(0) != null){
				buffer.append(address.getAddressLine(0));
			}
			
			if(address.getAddressLine(1) != null){
				buffer.append(", ");
				buffer.append(address.getAddressLine(1));
			}
			
			if(address.getAddressLine(2) != null){
				buffer.append(", ");
				buffer.append(address.getAddressLine(2));
			}			
		}
		
		return buffer.toString();
	}
	
	public ArrayList<Address> getArrayList(){
		return resultList;
	}
	
	
	@Override
	public Filter getFilter() {
		return new Filter() {
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				  if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					
					if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = (ArrayList<Address>)autocomplete(constraint.toString());

	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
			}
		};
	}
	
	public List<Address> autocomplete(String input){
		Geocoder geocoder = new Geocoder(getContext());
		List<Address> addresses = null;
		
		try {
			
			addresses = geocoder.getFromLocationName(input, getContext().getResources().getInteger(R.integer.MAXRESULTSAUTOCOMPLETE));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addresses;
	}

}
