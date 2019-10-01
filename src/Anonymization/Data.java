package Anonymization;
public class Data {

	
	private String date;
	private String mac;
	private String location;
	private String encodedMac;
	
	
	//===================== Getters and Setters
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMac() {
		return mac;
	}
	public void setMacAdrress(String mac) {
		this.mac = mac;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public String getEncodedMac() {
		return encodedMac;
	}

	public void setEncodedMac(String encodedMac) {
		this.encodedMac = encodedMac;
	}


}
