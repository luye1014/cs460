import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*=============================================================================
 |   Assignment: Program # part1
 |    File Name: Prog1A.java
 |       Author: Lu Ye
 |
 |       Course: CSc 460 
 |   Instructor: L. McCann
 | Sect. Leader: Yawen Chen and Jacob Combs
 |     Due Date: January 25 th, 2017, at the beginning of class
 |
 |     Language:  Java
 |     Packages:  java.io
 |
 +-----------------------------------------------------------------------------
 |	Description: Part A  
 |		Switch the file end with csv to the ninary file which ends with bin. That is, keep the file name
 |		And create it in the current directory.
 | 		Field types are limited to int, double, and String. All Strings field need to be padded to the needed length.
 |		For each column, all values must consume the same quantity of bytes. This is easy for numeric columns,
 |		but for String columns we don’t want to waste storage. For those columns, you need to determine the
 |		number of characters in the longest value, and use that to size each value in the column for storage. This
 |		must be done for each execution of the program.
 |
 |	Techniques:  The program's steps are as follows:
 |   	Read text file name and transfer it to binary file
 |	   	Read file line by line except reading the first line
 |     	Sort the memory by the fourth field of each line
 |	    Dump data into binary file
 |
 |   Known Bugs:  None
 |
 *===========================================================================*/

class DataRecord implements Comparable<DataRecord> {
	// 336string + 3int + 10double = 336+12+80
	public static final int RECORD_LENGTH = 428;
	private final int Trip_ID_Len = 40; // max length for trip id
	private final int Taxi_ID_Len = 128; // max length for taxi id
	private final int Trip_Start_Timestamp_Len = 22;
	private final int Trip_End_Timestamp_Len = 22;
	private final int Pickup_Census_Tract_Len = 11;
	private final int Dropoff_Census_Tract_Len = 11;
	private final int Payment_Type_Len = 11;
	private final int Company_Len = 33;
	private final int Pickup_Centroid_Location_Len = 29;
	private final int Dropoff_Centroid_Location_Len = 29;

	// The data fields that comprise a record of our file
	private String Trip_ID, Taxi_ID, Trip_Start_Timestamp, Trip_End_Timestamp, Pickup_Census_Tract,
			Dropoff_Census_Tract, Payment_Type, Company, Pickup_Centroid_Location, Dropoff_Centroid_Location;
	private int Trip_Seconds, Pickup_Community_Area, Dropoff_Community_Area;
	private double Trip_Miles, Fare, Tips, Tolls, Extras, Trip_Total, Pickup_Centroid_Latitude,
			Pickup_Centroid_Longitude, Dropoff_Centroid_Latitude, Dropoff_Centroid_Longitude;

	/**
	 * Method: public String get() public int get() public double get() | We
	 * have three types which is String, int and double These are all 'Getters'
	 * for the data field values
	 * =========================================================================
	 */
	// public public String getTrip_ID()
	public String getTrip_ID() {
		return this.Trip_ID;
	}

	// get taxi ID
	public String getTaxi_ID() {
		return this.Taxi_ID;
	}

	// get getTrip_Start_Timestamp()
	public String getTrip_Start_Timestamp() {
		return this.Trip_Start_Timestamp;
	}

	// getTrip_End_Timestamp()
	public String getTrip_End_Timestamp() {
		return this.Trip_End_Timestamp;
	}

	// getTrip_Seconds()
	public int getTrip_Seconds() {
		return this.Trip_Seconds;
	}

	// getTrip_Miles()
	public double getTrip_Miles() {
		return this.Trip_Miles;
	}

	// getPickup_Census_Tract()
	public String getPickup_Census_Tract() {
		return this.Pickup_Census_Tract;
	}

	// getDropoff_Census_Tract()
	public String getDropoff_Census_Tract() {
		return this.Dropoff_Census_Tract;
	}

	// getPickup_Community_Area()
	public int getPickup_Community_Area() {
		return this.Pickup_Community_Area;
	}

	// getDropoff_Community_Area()
	public int getDropoff_Community_Area() {
		return this.Dropoff_Community_Area;
	}

	// getFare()
	public double getFare() {
		return this.Fare;
	}

	// getTips()
	public double getTips() {
		return this.Tips;
	}

	// getTolls()
	public double getTolls() {
		return this.Tolls;
	}

	// getExtras()
	public double getExtras() {
		return this.Extras;
	}

	// getTrip_Total()
	public double getTrip_Total() {
		return this.Trip_Total;
	}

	// getPayment_Type()
	public String getPayment_Type() {
		return this.Payment_Type;
	}

	// getCompany()
	public String getCompany() {
		return this.Company;
	}

	// getPickup_Centroid_Latitude()
	public double getPickup_Centroid_Latitude() {
		return this.Pickup_Centroid_Latitude;
	}

	// getPickup_Centroid_Longitude()
	public double getPickup_Centroid_Longitude() {
		return this.Pickup_Centroid_Longitude;
	}

	// getPickup_Centroid_Location()
	public String getPickup_Centroid_Location() {
		return this.Pickup_Centroid_Location;
	}

	// getDropoff_Centroid_Location()
	public String getDropoff_Centroid_Location() {
		return this.Dropoff_Centroid_Location;
	}

	// getDropoff_Centroid_Latitude()
	public double getDropoff_Centroid_Latitude() {
		return this.Dropoff_Centroid_Latitude;
	}

	// getDropoff_Centroid_Longitude()
	public double getDropoff_Centroid_Longitude() {
		return this.Dropoff_Centroid_Longitude;
	}



	/**
	 * Method: public String set() public int set() public double set() | We
	 * have three types which is String, int and double These are all 'Setters'
	 * for the data field values
	 * =========================================================================
	 */
	// setTrip_ID
	public void setTrip_ID(String Trip_ID) {
		this.Trip_ID = Trip_ID;
	}

	// setTaxi_ID
	public void setTaxi_ID(String Taxi_ID) {
		this.Taxi_ID = Taxi_ID;
	}

	// setTrip_Start_Timestamp
	public void setTrip_Start_Timestamp(String Trip_Start_Timestamp) {
		this.Trip_Start_Timestamp = Trip_Start_Timestamp;
	}

	// setTrip_End_Timestamp
	public void setTrip_End_Timestamp(String Trip_End_Timestamp) {
		this.Trip_End_Timestamp = Trip_End_Timestamp;
	}

	// setTrip_Seconds
	public void setTrip_Seconds(int Trip_Seconds) {
		this.Trip_Seconds = Trip_Seconds;
	}

	// setTrip_Miles
	public void setTrip_Miles(double Trip_Miles) {
		this.Trip_Miles = Trip_Miles;
	}

	// setPickup_Census_Tract
	public void setPickup_Census_Tract(String Pickup_Census_Tract) {
		this.Pickup_Census_Tract = Pickup_Census_Tract;
	}

	// setDropoff_Census_Tract
	public void setDropoff_Census_Tract(String Dropoff_Census_Tract) {
		this.Dropoff_Census_Tract = Dropoff_Census_Tract;
	}

	// setPickup_Community_Area
	public void setPickup_Community_Area(int Pickup_Community_Area) {
		this.Pickup_Community_Area = Pickup_Community_Area;
	}

	// setDropoff_Community_Area
	public void setDropoff_Community_Area(int Dropoff_Community_Area) {
		this.Dropoff_Community_Area = Dropoff_Community_Area;
	}

	// setFare
	public void setFare(double Fare) {
		this.Fare = Fare;
	}

	// setTips
	public void setTips(double Tips) {
		this.Tips = Tips;
	}

	// setTolls
	public void setTolls(double Tolls) {
		this.Tolls = Tolls;
	}

	// setTrip_Total
	public void setExtras(double Extras) {
		this.Extras = Extras;
	}

	// setTrip_Total
	public void setTrip_Total(double Trip_Total) {
		this.Trip_Total = Trip_Total;
	}

	// setPayment_Type
	public void setPayment_Type(String Payment_Type) {
		this.Payment_Type = Payment_Type;
	}

	// setCompany
	public void setCompany(String Company) {
		this.Company = Company;
	}

	// setPickup_Centroid_Latitude
	public void setPickup_Centroid_Latitude(double Pickup_Centroid_Latitude) {
		this.Pickup_Centroid_Latitude = Pickup_Centroid_Latitude;
	}

	// setPickup_Centroid_Longitude
	public void setPickup_Centroid_Longitude(double Pickup_Centroid_Longitude) {
		this.Pickup_Centroid_Longitude = Pickup_Centroid_Longitude;
	}

	// setPickup_Centroid_Location
	public void setPickup_Centroid_Location(String Pickup_Centroid_Location) {
		this.Pickup_Centroid_Location = Pickup_Centroid_Location;
	}

	// setDropoff_Centroid_Location
	public void setDropoff_Centroid_Location(String Dropoff_Centroid_Location) {
		this.Dropoff_Centroid_Location = Dropoff_Centroid_Location;
	}

	// setDropoff_Centroid_Latitude
	public void setDropoff_Centroid_Latitude(double Dropoff_Centroid_Latitude) {
		this.Dropoff_Centroid_Latitude = Dropoff_Centroid_Latitude;
	}

	// setDropoff_Centroid_Longitude
	public void setDropoff_Centroid_Longitude(double Dropoff_Centroid_Longitude) {
		this.Dropoff_Centroid_Longitude = Dropoff_Centroid_Longitude;
	}

	/**
	 * Method: public String padString(String str, int size) { Description:
	 * Given a size and a string you want to expand the string to needed size
	 * =========================================================================
	 * ==
	 */
	public String padString(String str, int size) {
		while (str.length() < size) {
			str += " ";
		}
		return str; // return a new string with some tabs

	}

	/*
	 * =========================================================================
	 * ==== dumpObject(stream) -- write the content of the object's fields to
	 * the file represented by the given RandomAccessFile object reference.
	 * Primitive types (e.g., int) are written directly. Non-fixed-size values
	 * (e.g., strings) are converted to the maximum allowed size before being
	 * written. The result is a file of uniformly-sized records. Also note that
	 * text is written with just one byte per character, meaning that we are not
	 * supporting Unicode text.
	 * =========================================================================
	 * ====
	 */
	public void dumpObject(RandomAccessFile stream) {
		StringBuffer Tid = new StringBuffer(padString(Trip_ID, Trip_ID_Len)); // paddable
		StringBuffer Taid = new StringBuffer(padString(Taxi_ID, Taxi_ID_Len));// paddable
		StringBuffer TST = new StringBuffer(padString(Trip_Start_Timestamp, Trip_Start_Timestamp_Len));// paddable
		StringBuffer TET = new StringBuffer(padString(Trip_End_Timestamp, Trip_End_Timestamp_Len));// paddable
		StringBuffer PCensusT = new StringBuffer(padString(Pickup_Census_Tract, Pickup_Census_Tract_Len));// paddable
		StringBuffer DCensusT = new StringBuffer(padString(Dropoff_Census_Tract, Dropoff_Census_Tract_Len));// paddable
		StringBuffer PT = new StringBuffer(padString(Payment_Type, Payment_Type_Len));// paddable
		StringBuffer C = new StringBuffer(padString(Company, Company_Len));// paddable
		StringBuffer PCentroidLo = new StringBuffer(Pickup_Centroid_Location);// paddable
		StringBuffer DCentroidLo = new StringBuffer(Dropoff_Centroid_Location);// paddable
		try {
			// if you have string then set the length to the length you needed
			Tid.setLength(Trip_ID_Len);
			stream.writeBytes(Tid.toString());// write in bytes
			// if you have string then set the length to the length you needed
			Taid.setLength(Taxi_ID_Len);
			stream.writeBytes(Taid.toString());
			// if you have string then set the length to the length you needed
			TST.setLength(Trip_Start_Timestamp_Len);
			stream.writeBytes(TST.toString());
			// if you have string then set the length to the length you needed
			TET.setLength(Trip_End_Timestamp_Len);
			stream.writeBytes(TET.toString());

			stream.writeInt(Trip_Seconds);// write in Int
			stream.writeDouble(Trip_Miles);// write in double

			PCensusT.setLength(Pickup_Census_Tract_Len);// same as above if your
														// variable is string
			stream.writeBytes(PCensusT.toString());
			// if you have string then set the length to the length you needed
			DCensusT.setLength(Dropoff_Census_Tract_Len);
			stream.writeBytes(DCensusT.toString());// write in bytes

			stream.writeInt(Pickup_Community_Area);// write int
			stream.writeInt(Dropoff_Community_Area);

			stream.writeDouble(Fare); // write double
			stream.writeDouble(Tips); // write double
			stream.writeDouble(Tolls); // write double
			stream.writeDouble(Extras); // write double
			stream.writeDouble(Trip_Total);// write double

			// if you have string then set the length to the length you needed
			PT.setLength(Payment_Type_Len);
			stream.writeBytes(PT.toString());// write in bytes
			// if you have string then set the length to the length you needed
			C.setLength(Company_Len);
			stream.writeBytes(C.toString());// write in bytes

			stream.writeDouble(Pickup_Centroid_Latitude);// write double
			stream.writeDouble(Pickup_Centroid_Longitude);// write double
			// if you have string then set the length to the length you needed
			PCentroidLo.setLength(Pickup_Centroid_Location_Len);
			stream.writeBytes(PCentroidLo.toString());// write in bytes

			stream.writeDouble(Dropoff_Centroid_Latitude);// write double
			stream.writeDouble(Dropoff_Centroid_Longitude);// write double

			// if you have string then set the length to the length you needed
			DCentroidLo.setLength(Dropoff_Centroid_Location_Len);
			stream.writeBytes(DCentroidLo.toString());// write in bytes
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't write to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
	}

	/*
	 * =========================================================================
	 * ==== fetchObject(stream) -- read the content of the object's fields from
	 * the file represented by the given RandomAccessFile object reference,
	 * starting at the current file position. Primitive types (e.g., int) are
	 * read directly. To create Strings containing the text, because the file
	 * records have text stored with one byte per character, we can read a text
	 * field into an array of bytes and use that array as a parameter to a
	 * String constructor.
	 * =========================================================================
	 * ====
	 */

	public void fetchObject(RandomAccessFile stream) {
		byte[] tripID = new byte[Trip_ID_Len]; // file -> byte[] -> String
		byte[] taxiID = new byte[Taxi_ID_Len]; // file -> byte[] -> String
		byte[] tst = new byte[Trip_Start_Timestamp_Len];// file -> byte[] ->
														// String
		byte[] tet = new byte[Trip_End_Timestamp_Len];// file -> byte[] ->
														// String
		byte[] pct = new byte[Pickup_Census_Tract_Len]; // file -> byte[] ->
														// String
		byte[] dct = new byte[Dropoff_Census_Tract_Len]; // file -> byte[] ->
															// String
		byte[] pt = new byte[Payment_Type_Len]; // file -> byte[] -> String
		byte[] co = new byte[Company_Len]; // file -> byte[] -> String
		byte[] pcl = new byte[Pickup_Centroid_Location_Len]; // file -> byte[]
																// -> String
		byte[] dcl = new byte[Dropoff_Centroid_Location_Len]; // file -> byte[]
																// -> String

		try {
			stream.readFully(tripID); // read fully for string variable
			Trip_ID = new String(tripID);
			stream.readFully(taxiID); // read fully for string variable
			Taxi_ID = new String(taxiID);
			stream.readFully(tst); // read fully for string variable
			Trip_Start_Timestamp = new String(tst);// transfer it to a new
													// string
			stream.readFully(tet);
			Trip_End_Timestamp = new String(tet);

			Trip_Seconds = stream.readInt();// read int
			Trip_Miles = stream.readDouble();// read double

			stream.readFully(pct);
			Pickup_Census_Tract = new String(pct);
			stream.readFully(dct);
			Dropoff_Census_Tract = new String(dct);

			Pickup_Community_Area = stream.readInt();// read from int
			Dropoff_Community_Area = stream.readInt();// read from int

			Fare = stream.readDouble();// read from double
			Tips = stream.readDouble();// read from double
			Tolls = stream.readDouble();// read from double
			Extras = stream.readDouble();// read from double
			Trip_Total = stream.readDouble();// read from double

			stream.readFully(pt); // read fully for string variable
			Payment_Type = new String(pt);
			stream.readFully(co);
			Company = new String(co);

			Pickup_Centroid_Latitude = stream.readDouble();// read from double
			Pickup_Centroid_Longitude = stream.readDouble();// read from double
			stream.readFully(pcl);
			Pickup_Centroid_Location = new String(pcl);

			Dropoff_Centroid_Latitude = stream.readDouble();// read from double
			Dropoff_Centroid_Longitude = stream.readDouble();// read from double
			stream.readFully(dcl); // read fully for string variable
			Dropoff_Centroid_Location = new String(dcl);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
	}

	/*
	 * Method: public int compareTo(DataRecord other) { Description: sort
	 * according to trip id
	 */
	@Override
	public int compareTo(DataRecord other) {
		// TODO Auto-generated method stub
		return this.Trip_ID.compareTo(other.Trip_ID);
	}

}

/*
 * Main class for Prog1A. For an input file named file.csv, name the binary file
 * file.bin. (That is, keep the file name, but change the extension.) Don’t put
 * a path on it; just create it in the current directory.) Field types are
 * limited to int, double, and String. All money fields are to be doubles, and
 * pad Strings on the right with spaces to reach the needed length(s). (For
 * example, "abc ".) For each column, all values must consume the same quantity
 * of bytes. This is easy for numeric columns, but for String columns we don’t
 * want to waste storage. For those columns, you need to determine the number of
 * characters in the longest value, and use that to size each value in the
 * column for storage. This must be done for each execution of the program.
 * (Why? The data doesn’t provide field sizes, so we need to code defensively to
 * accommodate unexpected changes.)
 */
public class Prog1A {
	/*
	 * Main method of the assignment Logic: Read file and transfer it to binary
	 * file first dump the line you read into binary file
	 */
	public static void main(String[] args) throws IOException {
		File fileRef; // used to create the file
		RandomAccessFile dataStream = null; // specializes the file I/O
		DataRecord curRecord = null; // the objects to write/read
		long numberOfRecords = 0; // loop counter for reading file
		ArrayList<DataRecord> records = new ArrayList<DataRecord>(); // store
																		// record

		String textName = ""; // file name to read plain text
		Scanner sc = new Scanner(args[0]);
		BufferedReader reader = null;
		textName = sc.next();
		try {
			reader = new BufferedReader(new FileReader(textName));
		} catch (IOException e) {
			System.out.println("No such file or directory");
			System.exit(-1);
		}
		String line = null;

		sc.close();

		reader.readLine(); // to read the first line and do nothing.
		/*
		 * Create a File object to represent the file and a RandomAccessFile
		 * (RAF) object to supply appropriate file access methods. Note that
		 * there is a constructor available for creating RAFs directly (w/o
		 * needing a File object first), but having access to File object
		 * methods is often handy.
		 */

		fileRef = new File(textName.substring(0, (textName.length() - 3)) + "bin"); // replace
																					// csv
																					// to
																					// bin
		// fileRef = new File("test3.bin");
		try {
			dataStream = new RandomAccessFile(fileRef, "rw");
		} catch (IOException e) {
			System.out
					.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}

		// Tell the DataRecord objects to write themselves
		String[] field;

		while ((line = reader.readLine()) != null) {
			// only have 23 fields
			field = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 23);
			curRecord = new DataRecord();

			// check missing
			if (field[4].isEmpty()) {
				field[4] = "-1";
			}
			// check if these field are missing
			if (field[5].isEmpty()) {
				field[5] = "-1";
			}
			// check if these field are missing
			if (field[8].isEmpty()) {
				field[8] = "-1";
			}
			// check if these field are missing
			if (field[9].isEmpty()) {
				field[9] = "-1";
			}

			curRecord.setTrip_ID(field[0]); // set tripid to field 0
			curRecord.setTaxi_ID(field[1]); // set texiid to field 1
			// set trip start timestamo to field 2
			curRecord.setTrip_Start_Timestamp(field[2]);
			// set trip end timestamp to field 3
			curRecord.setTrip_End_Timestamp(field[3]);
			// set trip seconds to field 4
			curRecord.setTrip_Seconds(Integer.parseInt(field[4]));
			// set trip miles to field 5
			curRecord.setTrip_Miles(Double.parseDouble(field[5]));
			// set pick census tract to field 6
			curRecord.setPickup_Census_Tract(field[6]);
			// set dropoff census tract to field 7
			curRecord.setDropoff_Census_Tract(field[7]);
			// set pickup community area to field8
			curRecord.setPickup_Community_Area(Integer.parseInt(field[8]));
			// set dropoff co arean to filed 9
			curRecord.setDropoff_Community_Area(Integer.parseInt(field[9]));

			// field[10] = field[10].substring(1);

			for (int i = 10; i < 15; i++) {
				if (field[i].isEmpty() == false) {
					// otherwise replace all the dollar sign
					field[i] = field[i].replaceAll("\\$", "");
				} else {
					field[i] = "-1";
				}
			}

			curRecord.setFare(Double.parseDouble(field[10]));
			// transfer double to string and put it in curRecord
			curRecord.setTips(Double.parseDouble(field[11]));
			// transfer double to string and put it in curRecord
			curRecord.setTolls(Double.parseDouble(field[12]));
			// transfer double to string and put it in curRecord
			curRecord.setExtras(Double.parseDouble(field[13]));
			// transfer double to string and put it in curRecord
			curRecord.setTrip_Total(Double.parseDouble(field[14]));
			// transfer double to string and put it in curRecord

			curRecord.setPayment_Type(field[15]);
			curRecord.setCompany(field[16]);

			// check if these field are missing
			if (field[17].isEmpty()) {
				field[17] = "-1";
			}
			// check if these field are missing
			if (field[18].isEmpty()) {
				field[18] = "-1";
			}
			// check if these field are missing
			if (field[20].isEmpty()) {
				field[20] = "-1";
			}
			// check if these field are missing
			if (field[21].isEmpty()) {
				field[21] = "-1";
			}
			// put it in curRecord
			curRecord.setPickup_Centroid_Latitude(Double.parseDouble(field[17]));
			// put it in curRecord
			curRecord.setPickup_Centroid_Longitude(Double.parseDouble(field[18]));
			// put it in curRecord
			curRecord.setPickup_Centroid_Location(field[19]);
			// put it in curRecord
			curRecord.setDropoff_Centroid_Latitude(Double.parseDouble(field[20]));
			// put it in curRecord
			curRecord.setDropoff_Centroid_Longitude(Double.parseDouble(field[21]));
			// put it in curRecord
			curRecord.setDropoff_Centroid_Location(field[22]);
			records.add(curRecord);// add currecord to records
		}
		reader.close(); // end reading

		Collections.sort(records);
		for (int i = 0; i < records.size(); i++) {
			records.get(i).dumpObject(dataStream);
		}

		// dataStream = new RandomAccessFile(fileRef, "r");

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

		// print record
		System.out.println("\nThere are " + numberOfRecords + " records in the file.\n");

		// while (numberOfRecords > 0) {
		// rec1.fetchObject(dataStream);
		// System.out.println("Read state code of " + rec1.getStateCode());
		// System.out.println("Read place code of " + rec1.getPlaceCode());
		// System.out.println("Read county name of " + rec1.getCountyName());
		// System.out.println();
		// numberOfRecords--;
		// }

		// Clean-up by closing the file

		try {
			dataStream.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
	}
}
