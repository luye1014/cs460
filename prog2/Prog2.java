import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

class Entry {
	
	private int num;
	private int pointer;
	//private final int Trip_Total_Len = 7; // max length for trip—total is 7
	/**
	 * public int getTripTotal()
	 * The getter of tripTotal
	 * @return
	 */
	public int getTripTotal(){
		return num;
	}
	/**
	 * public int getpointer()
	 * The getter of pointer
	 * @return
	 */
	public int getPointer(){
		return pointer;
	}
	/**
	 * public void setTripTotal(int tripTotal)
	 * The setter for tripTotal
	 * @param tripTotal
	 */
	public void setTripTotal(int tripTotal_num){
		this.num = tripTotal_num;
	}
	/**
	 * public void setPointer(int pointer){
	 * The setter for pointer
	 * @param pointer
	 */
	public void setPointer(int pointer){
		this.pointer = pointer;
	}
	
	/**
	 * dumpObject(stream) -- write the content of the object's fields to
	 * the file represented by the given RandomAccessFile object reference.
	 * Primitive types (e.g., int) are written directly. Non-fixed-size values
	 * (e.g., strings) are converted to the maximum allowed size before being
	 * written. The result is a file of uniformly-sized records. Also note that
	 * text is written with just one byte per character, meaning that we are not
	 * supporting Unicode text.
	 */
	public void dumpObject(RandomAccessFile stream){
		//StringBuffer tripT = new StringBuffer();
		try {
			stream.writeInt(pointer);
			stream.writeInt(pointer);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't write Entry to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
	}
	/**
	 * fetchObject(stream) -- read the content of the object's fields from
	 * the file represented by the given RandomAccessFile object reference,
	 * starting at the current file position. Primitive types (e.g., int) are
	 * read directly. To create Strings containing the text, because the file
	 * records have text stored with one byte per character, we can read a text
	 * field into an array of bytes and use that array as a parameter to a
	 * String constructor.
	 */
	public void fetchObject(RandomAccessFile stream){
		try {
			// read fully for string variable
//			stream.readFully(tripT);
			num = stream.readInt();
			//read pointer as int
			pointer = stream.readInt();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read Entry to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
	}
}
public class Prog2 {
	public static void main(String[] args) throws IOException {
		RandomAccessFile dataStream = null; //intialize the I/O file
		String textname = "";
		Scanner sc = new Scanner(args[0]); //read from the terminal command line
		textname = sc.next();    //the file you read
		
		try {
			dataStream = new RandomAccessFile(textname, "rw"); //read and write file
		} catch (IOException e) {
			System.out.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}
		
		/*
		 * Move the file pointer (which marks the byte with which the next
		 * access will begin) to the front of the file (that is, to byte 0).
		 */
		try {
			dataStream.seek(0);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't reset the file " + "pointer to the start of the file.");
			System.exit(-1);
		}
		ArrayList<int[]> directory = new ArrayList<int[]>();
		
		RandomAccessFile bucketStream = null;
		
		DataRecord data = new DataRecord();
		long numberOfRecords = dataStream.length() / DataRecord.RECORD_LENGTH;
		System.out.println(numberOfRecords);
		int counter = 0;
		while(numberOfRecords-1>=0){
			seekHelper(data, dataStream, numberOfRecords-1);
			addRecord(directory, bucketStream, data);
			counter++; // the number of Records
			
			//System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
			numberOfRecords--;
			
		}

		System.out.println(counter);
//		seekHelper(data, dataStream, 0);
//		System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
//		bucketsStream.close();	
//		bucketsStream = new RandomAccessFile(bucketFile, "rw");
		
		//give the number you want to search
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Give the sufix you want to search: ");
//		
//		boolean hasNext = true;
//		while(hasNext == true){		
//			boolean find = false;
//			if(!scanner.hasNextInt()){								//check if the input is integer or not
//				System.out.println("The input must be integer.\n");
//				hasNext = false;
//				break;
//			}
//			int target = scanner.nextInt();
//			if(bucketsStream.length() == 0){
//				System.out.println("the file is empty.");
//				break;
//			}
//			int dirSize = (int)Math.ceil(((bucketsStream.length()/Entry.ENTRY_LENGTH)/10));
//			long index = target % dirSize;						//index represent which bucket we should search for
//			ArrayList<Entry> bucket = readWholeBuckets(index, bucketsStream);
//			bucketsStream.seek(index * 80);					//each bucket has 10 entries and each entry has 8 bytes
//			for(int n = 0; n < bucket.size(); n++){
//				Entry a = new Entry();
//				a.fetchObject(bucketsStream);
//				if(a.getTripTotal() == target){
//					DataRecord data = FindIndex(dataStream, a.getPointer());
//					System.out.println(data.getTrip_ID() + data.getTrip_Total() + "\n");
//					find = true;
//					break;
//				}
//			}
//			if(find == false){
//				System.out.println("Cannot find the data you want.");
//			}
//		}
//	}

	}
	
	private static void addRecord(ArrayList<int[]> directory, RandomAccessFile bucketStream, DataRecord data) {
		if(directory.size() < 11){ //0,1,2,3,4,5,6,7,8,9 = 10
			String target = convertToStr(data.getTrip_Total());
			for(int i = 0; i < 11; i++){
				if(Integer.parseInt(String.valueOf(target.charAt(target.length()-1))) == i){
					
				}
			}
		}
		
	}
	
	/**
	 * private static void convertToStr(double trip_Total)
	 * convert to String and also replace all of point
	 * @param trip_Total
	 * @return 
	 */
	private static String convertToStr(double trip_Total) {
		String temp = Double.toString(trip_Total);
		String result = temp.replaceAll("\\.", "");
		return result;
	}

	/**
	 * private static DataRecord seekHelper Find the location of the data and
	 * then fetch it out
	 * 
	 * @param dataR
	 * @param dataStream
	 * @param index
	 * @return
	 * @throws IOException
	 */
	private static DataRecord seekHelper(DataRecord dataR, RandomAccessFile dataStream, long index) throws IOException {
		// TODO Auto-generated method stub
		// dataStream.seek(0);
		dataStream.seek(index * DataRecord.RECORD_LENGTH);
		dataR.fetchObject(dataStream);
		// dataStream.seek(0);
		return dataR;
	}

}
