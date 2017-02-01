import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*=============================================================================
|   Assignment: Program # part2
|    File Name: Prog1B.java
|       Author: Lu Ye
|
|       Course: CSc 460 
|   Instructor: L. McCann
| Sect. Leader: Yawen Chen and Jacob Combs
|     Due Date: Feb 2 th, 2017, at the beginning of class
|
|     Language:  Java
|     Packages:  java.io
|
+-----------------------------------------------------------------------------
|	Description: Part B  
|		1.Reads and prints to the screen the Trip IDs and Trip Totals of the first five records of data, the middle
| 		record (or both middle records, if the quantity of records is even), and the last five records of data in the
| 		binary file, plus the total number of records in the binary file.
| 		2. Using one or more Trip ID prefixes given by the user, locates within the binary file (using interpolation
| 		search; see below) and displays to the screen the Trip IDs and Trip Totals of all records having that Trip
| 		ID prefix
|
|	Input: if the user input "002a(Space)012b", consider it as two prefixes
|
|	Techniques:  The program's steps are as follows:
|   	Read text from binary file
|	   	Print first five, last five and middle record, plus total number of records
|     	Given a prefixes and search it, print all of records with that prefixes
|	    
|
|   Known Bugs:  None
|
*===========================================================================*/

public class Prog1B {

	/*
	 * Main method of the assignment Logic: Read file and transfer it to binary
	 * file first dump the line you read into binary file
	 */
	static boolean searchfound = false;
	// static ArrayList<DataRecord> result = new ArrayList<DataRecord>();
	static ArrayList<String> result = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		File fileRef = null; // used to create the file
		RandomAccessFile dataStream = null; // specializes the file I/O
		long numberOfRecords = 0; // loop counter for reading file

		String textName = ""; // file name to read plain text
		Scanner sc = new Scanner(args[0]);
		textName = sc.next();

		sc.close();

		dataStream = new RandomAccessFile(textName, "r");
		// below is the stream for IDE testing
		// dataStream = new
		// RandomAccessFile("/Users/kanoutsuyu/Documents/CSC352&345/cs460/src/test1_withFive.bin",
		// "r");

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

		/*
		 * Read the records and display their content to the screen.
		 */

		try {
			numberOfRecords = dataStream.length() / DataRecord.RECORD_LENGTH;
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't get the file's length.");
			System.exit(-1);
		}

		if (numberOfRecords == 0) {
			System.out.println("There is no record in this File. The program ends, try some other files.");
			System.exit(-1);
		}

		// Print the first five records with trip id and trio totals
		System.out.println("First Five Records with Trip IDs and Trip Totals are:\n");
		printFirstFiveRecords(dataStream, numberOfRecords);

		// print the middle record
		System.out.println("======================================================");
		System.out.println("The middle Record with Trip IDs and Trip Totals is:\n");
		printMiddleFiveRecords(dataStream, numberOfRecords);

		// print the last five records
		System.out.println("======================================================");
		System.out.println("Last Five Records with Trip IDs and Trip Totals are:\n");
		printLastFiveRecords(dataStream, numberOfRecords);
		System.out.println("======================================================");

		// print numberOfRecords
		System.out.println("\nThere are " + numberOfRecords + " records in the file.\n");
		System.out.println("======================================================");
		System.out.println("Insert prefixes to locates within the binary file:");
		// boolean hasNext = true;
		Scanner input = new Scanner(System.in);
		while (input.hasNext()) {
			searchfound = false;
			result = new ArrayList<String>();
			String prefixes = input.next();
			if (prefixes.length() > 40) {
				System.out.println("Your prefixs is too long, can't find the record");
			}
			else if(notValid(prefixes)){
				System.out.println("Invalid prefixes, Please reenter.");
			}
			else {
				DataRecord tem = new DataRecord();
				DataRecord tem2 = new DataRecord();
				seekHelper(tem, dataStream, 0);
				seekHelper(tem2, dataStream, numberOfRecords - 1);
				// check the first data record and the last data record
				// if first data record is satisfying the requirement, then
				// print result
				if (tem.getTrip_ID().substring(0, prefixes.length()).equals(prefixes)) {
					leftHelper(dataStream, prefixes, 0);
					rightHelper(dataStream, prefixes, 0);
					// else if last data record is satisfying the requirement,
					// then print
				} else if (tem2.getTrip_ID().substring(0, prefixes.length()).equals(prefixes)) {
					leftHelper(dataStream, prefixes, numberOfRecords - 1);
					rightHelper(dataStream, prefixes, numberOfRecords - 1);
					// other wise using interpolation search to search in file
				} else
					InterpolationSearch(fileRef, dataStream, 0, numberOfRecords - 1, prefixes);
				// below is another way using binary search
				// ISearch(fileRef, dataStream, -1, numberOfRecords, prefixes);
				if (searchfound == false) {
					System.out.println("I am sorry, we can't find matching record.");
				} else {
					// sort result and print
					Collections.sort(result);
					// System.out.println(result);
					StringBuilder builder = new StringBuilder();
					for (String value : result) {
						builder.append(value);
					}
					// String text = builder.toString();
					System.out.println(builder.toString());
				}
			}
			System.out.println("If you don't want to insert anymore, press control + D.");
		}

		input.close();
		// Clean-up by closing the file

		try {
			dataStream.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
	}

	/**
	 * public static int InterpolationSearch2(File fileRef, RandomAccessFile
	 * dataStream, long low_index, long high_index, String target) throws
	 * IOException { Interpolation Search based on the idea Interpolation Search
	 * is an enhanced binary search. To be most effective, these conditions must
	 * exist: (1) The data is stored in a direct-access data structure (such as
	 * a binary file of uniformly-sized records), (2) the data is in sorted
	 * order by the search key, (3) the data is uniformly distributed, and (4)
	 * there’s a lot of data. In such situations, the reduction in quantity of
	 * probes over binary search is likely to be particularly beneficial given
	 * the inherent delay that exists in file accesses. Our data falls short on
	 * (3) and (4), but that’s OK; the search will still work.
	 * 
	 * @param fileRef
	 * @param dataStream
	 * @param low_index
	 * @param high_index
	 * @param target
	 * @return
	 * @throws IOException
	 */

	public static void InterpolationSearch(File fileRef, RandomAccessFile dataStream, long low_index, long high_index,
			String target) throws IOException {
		int prefix_Len = target.length();
		DataRecord data = new DataRecord();
		DataRecord temp1 = new DataRecord();
		DataRecord temp2 = new DataRecord();
		//long numberOfRecords = dataStream.length() / DataRecord.RECORD_LENGTH;
		if (high_index - low_index > 0) {
			// seek for low_index record and high_index record
			seekHelper(temp1, dataStream, low_index);
			seekHelper(temp2, dataStream, high_index);
			// probe index
			long index = low_index
					+ (((diffOfStrings(target, temp1.getTrip_ID().substring(0, prefix_Len))) * (high_index - low_index))
							/ (diffOfStrings(temp2.getTrip_ID(), temp1.getTrip_ID())));

			seekHelper(data, dataStream, index);
			// if target is equal to data's record, which means users are found
			// the key
			if (data.getTrip_ID().substring(0, prefix_Len).equals(target)) {
				// traking that record to see whether the right side or left
				// side have more wanted records
				leftHelper(dataStream, target, low_index);
				rightHelper(dataStream, target, low_index);
			} else if (data.getTrip_ID().compareTo(target) < 0) {
				// other wise recuse the function
				InterpolationSearch(fileRef, dataStream, index + 1, high_index, target);
			} else {
				// other wise recuse the function
				InterpolationSearch(fileRef, dataStream, low_index, index - 1, target);
			}
		}
	}

	/**
	 * public static void InterpolationSearch Main method for searching the This
	 * is another search function I built, not used in this program and this
	 * project. Just for me to explore the time cost from Interpolation Search.
	 * And this is similar to Interpolation Search.
	 * 
	 * @param fileRef
	 * @param dataStream
	 * @param low_index
	 * @param high_index
	 * @param target
	 * @throws IOException
	 */
	public static void ISearch(File fileRef, RandomAccessFile dataStream, long low_index, long high_index,
			String target) throws IOException {
		// System.out.println(high_index);
		int prefix_Len = target.length();
		if (high_index - low_index > 1) {
			long mid_index = (low_index + high_index) / 2;
			DataRecord dr = new DataRecord();
			seekHelper(dr, dataStream, mid_index);
			// if target is equal to data's record, which means users are found
			// the key
			if (dr.getTrip_ID().substring(0, prefix_Len).equals(target)) {
				leftHelper(dataStream, target, mid_index);
				rightHelper(dataStream, target, mid_index);
			} else if (dr.getTrip_ID().compareTo(target) < 0) {
				ISearch(fileRef, dataStream, mid_index, high_index, target);
			} else {
				ISearch(fileRef, dataStream, low_index, mid_index, target);
			}
		}

	}

	/**
	 * private static boolean notValid(String p) {
	 * To check whether the prefixes is valid or not
	 * @param p
	 * @return
	 */
	private static boolean notValid(String p) {
		for(char c: p.toCharArray()){
			if((c>='0'&&c<='9')|| (c>='a'&&c<='f')){
				continue;
			}else{
				return true;
			}
		}
		return false;
	}

	/**
	 * private static long diffOfStrings(String target, String target2) find the
	 * difference, working for interpolation search
	 * 
	 * @param target
	 * @param target2
	 * @return
	 */

	private static long diffOfStrings(String target, String target2) {
		return toDec(target) - toDec(target2);
	}

	/**
	 * public static long toDec(String target) { Change hex string to decimal
	 * digit
	 * 
	 * @param target
	 * @return
	 */
	public static long toDec(String target) {
		long sum = 0;
		// tracking each char in char array
		for (char c : target.toCharArray()) {
			int i = target.indexOf(c);
			sum = sum * 16 + i;
		}
		return sum;

	}

	/**
	 * This method is a help method for searching the right side to check
	 * whether it can be found adapted record
	 * 
	 * @param dataStream
	 * @param target
	 * @param i
	 * @throws IOException
	 */
	public static void leftHelper(RandomAccessFile dataStream, String target, long i) throws IOException {
		int len = target.length();
		DataRecord dr = new DataRecord();
		seekHelper(dr, dataStream, i);
		while (dr.getTrip_ID().substring(0, len).equals(target)) {
			searchfound = true;
			// add all of proper data into result. so that we can sort the data
			// in order at the end
			result.add(dr.getTrip_ID() + " " + dr.getTrip_Total() + "\n");
			if (i - 1 >= 0) {
				seekHelper(dr, dataStream, i - 1);
				i--;
			} else {
				break;
			}
		}
	}

	/**
	 * public static void rightHelper This method is a help method for searching
	 * the right side to check whether it can be found adapted record
	 * 
	 * @param dataStream
	 * @param target
	 * @param i
	 * @throws IOException
	 */
	public static void rightHelper(RandomAccessFile dataStream, String target, long i) throws IOException {
		int len = target.length();
		DataRecord dr = new DataRecord();
		long numberOfRecords = dataStream.length() / DataRecord.RECORD_LENGTH;
		while (i + 1 < numberOfRecords) {
			seekHelper(dr, dataStream, i + 1);
			if (dr.getTrip_ID().substring(0, len).equals(target)) {
				searchfound = true;
				// add all of proper data into result. so that we can sort the
				// data in order at the end
				result.add(dr.getTrip_ID() + " " + dr.getTrip_Total() + "\n");
				// System.out.println(dr.getTrip_ID() +" " + dr.getTrip_Total()
				// + "\n"); //only print these two data
				i++;
			} else {
				break;
			}
		}
	}

	/**
	 * private static void printLastFiveRecords This is aim at printing the last
	 * five records
	 * 
	 * @param dataStream
	 * @param numberOfRecords
	 * @throws IOException
	 */
	private static void printLastFiveRecords(RandomAccessFile dataStream, long numberOfRecords) throws IOException {
		// if the number of records which is less than 5
		if (numberOfRecords < 5) {
			int count = (int) numberOfRecords;// total number of records
			while (count > 0) {
				DataRecord data = new DataRecord();
				seekHelper(data, dataStream, numberOfRecords - count);
				// only print these two data below
				System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
				count--;
			}
		} else {// if the number of record
			for (int i = 0; i < 5; i++) {
				DataRecord data = new DataRecord();
				seekHelper(data, dataStream, numberOfRecords - 5 + i);
				// only print these two data below
				System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
			}
		}

	}

	/**
	 * private static void printMiddleFiveRecords This is aim at printing the
	 * middle five records
	 * 
	 * @param dataStream
	 * @param numberOfRecords
	 * @throws IOException
	 */
	private static void printMiddleFiveRecords(RandomAccessFile dataStream, long numberOfRecords) throws IOException {
		int middleNum = (int) (numberOfRecords / 2);
		DataRecord data = new DataRecord();
		if (numberOfRecords % 2 == 1) {// The quantity of records is odd
			seekHelper(data, dataStream, middleNum);
			System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
		} else {// The quantity of records is even
			for (int i = 0; i < 2; i++) {
				seekHelper(data, dataStream, middleNum - 1 + i);
				System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
			}
		}
	}

	/**
	 * private static void printFirstFiveRecords This is aim at printing the
	 * first five record
	 * 
	 * @param dataStream
	 * @param numberOfRecords
	 * @throws IOException
	 */
	private static void printFirstFiveRecords(RandomAccessFile dataStream, long numberOfRecords) throws IOException {
		// if the number of records which is lessthan 5
		if (numberOfRecords < 5) {
			int count = (int) numberOfRecords;// total number of records
			while (count > 0) {
				DataRecord data = new DataRecord();
				seekHelper(data, dataStream, numberOfRecords - count);
				// only print these two data below
				System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
				count--;
			}
		} else {// if the number of record
			for (int i = 0; i < 5; i++) {
				DataRecord data = new DataRecord();
				seekHelper(data, dataStream, i);
				// only print these two data below
				System.out.println(data.getTrip_ID() + " " + data.getTrip_Total() + "\n");
			}
		}
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
