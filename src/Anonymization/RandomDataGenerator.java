package Anonymization;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class RandomDataGenerator {

	public static ArrayList<Data> readFile(String fileName) {
		RandomAccessFile raf = null;
		int row = 0;
		ArrayList<Data> data = new ArrayList<>();

		try {
			String[] parts;
			Data d;
			raf = new RandomAccessFile(fileName, "r");

			while (raf.getFilePointer() < raf.length()) {
				row++;
				parts = raf.readLine().split("\t");
				d = new Data();
				d.setDate(parts[0]);
				d.setMacAdrress(parts[1]);
				d.setLocation(parts[2]);

				data.add(d);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + " on row " + row);
		} finally {
			try {
				raf.close();
			} catch (Exception e) {
			}
		}
		return data;
	}

	// encrypts MAC addresses and prints them with other information to a text file
	public static void encrypts(ArrayList<Data> data) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

		// result file
		RandomAccessFile write = new RandomAccessFile("result.txt", "rw");

		// generates key
		String key = "JcJ6v50mI38=";
		byte[] byteKey = Base64.getDecoder().decode(key);
		SecretKey secretKey = new SecretKeySpec(byteKey, 0, byteKey.length, "DES");

		// initiates DES algorithm /w Electronic Codebook mode and PKCS #5-style padding
		Cipher DES = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DES.init(Cipher.ENCRYPT_MODE, secretKey);
	
		// loops through all data
		for(int i = 0; i < data.size(); i++) {

			// extracts hex numbers value from address and converts them to bytes
			byte plain[] = data.get(i).getMac().replaceAll(":", "").getBytes();
			
			// encrypts
			byte[] cipher = DES.doFinal(plain);

			// saves cipher text
			data.get(i).setEncodedMac(Base64.getEncoder().encodeToString(cipher));	
			write.writeBytes(data.get(i).getDate() + "\t" + data.get(i).getEncodedMac() + "\t" +
			data.get(i).getLocation() + "\r\n");
		}

		write.close();
	}

	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		ArrayList<Data> data = readFile("data.txt");
		encrypts(data);

	}

}
