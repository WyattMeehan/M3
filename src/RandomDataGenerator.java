import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class RandomDataGenerator {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		RandomAccessFile raf = null;
		int row = 0;
		ArrayList<Data> data = new ArrayList<>();
		RandomAccessFile write = new RandomAccessFile("result.txt", "rw");

		try {
			String [] parts;
			Data d;
			raf = new RandomAccessFile("data.txt", "r");

			while(raf.getFilePointer() < raf.length()) {
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
			try { raf.close(); } catch (Exception e) {}
		}
		
		// generates key
		String key = "JcJ6v50mI38=";
		byte[] byteKey = Base64.getDecoder().decode(key);
		SecretKey secretKey = new SecretKeySpec(byteKey, 0, byteKey.length, "DES");

		// uses DES algorithm /w Electronic Codebook mode and PKCS #5-style padding
		Cipher DES = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DES.init(Cipher.ENCRYPT_MODE, secretKey);

		
		for(int i = 0; i < data.size(); i++) {

			// encrypts
			byte[] text = data.get(i).getMac().getBytes();
			byte[] cipher = DES.doFinal(text);

			// saves cipher text
			data.get(i).setEncodedMac(Base64.getEncoder().encodeToString(cipher));

			
			write.writeBytes(data.get(i).getDate() + "\t" + data.get(i).getEncodedMac() + "\t" +
			data.get(i).getLocation() + "\n");
		}

		write.close();
	}

}
