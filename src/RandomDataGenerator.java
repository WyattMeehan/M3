import java.io.RandomAccessFile;
import java.util.ArrayList;

public class RandomDataGenerator {




	public static void main(String[] args){

		RandomAccessFile raf = null;
		int row = 0;
		ArrayList<Data> data = new ArrayList<>();

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
		
		for(Data d : data) {
			System.out.println(d.getMac());
		}
	}

}
