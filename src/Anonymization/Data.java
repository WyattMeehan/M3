package Anonymization;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Data {

	private String date;
	private String mac;
	private String location;
	private String encodedMac;
	private int[] signal;

	public Data(String date, String mac) {
		this.date = date;
		this.mac = mac;
		this.signal = new int[5];
		for (int i = 0; i < 5; i++)
		this.signal[i] = -512;
	}

	public Data() {

	}

	// ===================== Getters and Setters

	// sets signal for specific pi
	public void setSignal(int signal, int pi) {
		this.signal[pi] = signal;
	}

	public int[] getSignal() {
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

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
		String hex = Integer.toHexString(0xff & hash[i]);
		if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	// displays request with signal from all pis
	public String toString() {

		// hashes MAC address
		byte[] hash = new byte[1];
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			hash = digest.digest(mac.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String strength = "";
		for (int i : signal)
			strength = strength + "\t" + i;
		return date + "\t" + bytesToHex(hash) + strength;
	}

}
