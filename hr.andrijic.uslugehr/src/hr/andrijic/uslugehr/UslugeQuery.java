package hr.andrijic.uslugehr;

import android.location.Location;

public class UslugeQuery {
	private String SEARCH_KEYS = "search_keys";
	private String SEARCH_LOCATION = "search_location";	
	private String SEARCH_RADIUS = "search_radius";
	private String SEARCH_LIMIT = "search_limit";
	
	private String searchString;
	private Location searchLocation;
	private Integer radius;
	private Integer searchLimit;
	private String baseUrl;
	
	
	public UslugeQuery(String baseUrl, String searchString, Location searchLocation,
			Integer radius, Integer searchLimit) {
		super();
		this.baseUrl = baseUrl;
		this.searchString = searchString;
		this.searchLocation = searchLocation;
		this.radius = radius;
		this.searchLimit = searchLimit;
	}

	public String getQueryString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(baseUrl);
		
		if(searchString != null && searchString.length()>0){
			buffer.append(singleParam(SEARCH_KEYS, searchString));
		}
		
		if(searchLocation != null){
			
			buffer.append(singleParam(SEARCH_LOCATION, searchLocation.getLatitude()+":"+searchLocation.getLongitude()));
		}
		
		if(radius != null){
			buffer.append(singleParam(SEARCH_RADIUS, radius.toString()));
		}
		
		if(searchLimit != null){
			buffer.append(singleParam(SEARCH_LIMIT, searchLimit.toString()));
		}
		
		return buffer.toString();
	}
	
	public static String locationToString(Location location){
		 return new String(location.getLatitude()+":"+location.getLongitude());
	}
	
	public static String singleParam(String name, String value){
		StringBuffer buffer = new StringBuffer();
		
		if(name != null && value != null){
			buffer.append("&");
			buffer.append(name);
			buffer.append("=");
			buffer.append(value);
			buffer.append("&");
		}
		
		return buffer.toString();
	}
}
