package hr.andrijic.uslugehr;

import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class UslugaEntity {
	private String nid;
	private String title;
	private String status;
	private String comment;
	private String sticky;
	private String type;
	private String language;
	private String created;
	private String changed;
	private String body;
	private String author;
	private String email;
	private String telephone;
	private String total;
	private String delivery_time;
	private String price;
	private String customer_service;
	private String quality;
	private Location location=null;
	
	public UslugaEntity(JSONObject object){
		
		try{
			nid = object.getString("nid");
			title = object.getString("title");
			status = object.getString("status");
			comment = object.getString("comment");
			sticky = object.getString("sticky");
			type = object.getString("type");
			language = object.getString("language");
			created = object.getString("created");
			changed = object.getString("changed");
			body = object.getString("body");
			author = object.getString("author");
			telephone = object.getString("telephone");
			total = object.getString("total");
			delivery_time = object.getString("delivery_time");
			price = object.getString("price");
			customer_service = object.getString("customer_service");
			quality = object.getString("quality");
			email = object.getString("email");
			
			double latitude = object.getDouble("location_lat");
			double longitude = object.getDouble("location_lon");
			if(latitude != 0 && longitude != 0){
				location = new Location("fromuslugehr");
				location.setLongitude(longitude);
				location.setLatitude(latitude);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSticky() {
		return sticky;
	}
	public void setSticky(String sticky) {
		this.sticky = sticky;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getChanged() {
		return changed;
	}
	public void setChanged(String changed) {
		this.changed = changed;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCustomer_service() {
		return customer_service;
	}
	public void setCustomer_service(String customer_service) {
		this.customer_service = customer_service;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	
}
