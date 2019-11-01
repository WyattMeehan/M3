package Anonymization;
public class Data {

	
	private String date;
	private String mac;
	private String location;
	private String encodedMac;
	private int[] signal;
	
	public Data(String date, String mac){
		this.date = date;
		this.mac = mac;
		this.signal = new int[5];
	}

	public Data(){
		
	}
	
	//===================== Getters and Setters

	// sets signal for specific pi
	public void setSignal(int signal, int pi){
		this.signal[pi] = signal;
	}

	public int[] getSignal(){
		return signal;
	}
	
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

	// displays request with signal from all pis
	public String toString(){
		String strength = "";
		for (int i : signal)
			strength = strength + "\t" + i;
		return date + "\t" + mac + strength;
	}

}
