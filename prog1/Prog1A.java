import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 |		• Switch the file end with csv to the ninary file which ends with bin. That is, keep the file name
 |		And create it in the current directory.
 | 		• Field types are limited to int, double, and String. All Strings field need to be padded to the needed length.
 |		• For each column, all values must consume the same quantity of bytes. This is easy for numeric columns,
 |		but for String columns we don’t want to waste storage. For those columns, you need to determine the
 |		number of characters in the longest value, and use that to size each value in the column for storage. This
 |		must be done for each execution of the program.
 |
 |	Techniques:  The program's steps are as follows:
 |   	• Read text file name and transfer it to binary file
 |	   	• Read file line by line except reading the first line
 |     	• Sort the memory by the fourth field of each line
 |	    • Dump data into binary file
 |
 |   Known Bugs:  None
 |
 *===========================================================================*/

class DataRecord implements Comparable<DataRecord> {
	public static final int RECORD_LENGTH = 428;// 336string + 3int + 10double = 336+12+80
	private final int Trip_ID_Len = 40; //max length for trip id
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

	/*=============================================================================
	 |   Method: public String get()
	 |			 public int get()	
	 |			 public double get()
	 |	We have three types which is String, int and double
	 | These are all 'Getters' for the data field values
	 *===========================================================================*/
	public String getTrip_ID() {return this.Trip_ID;}
	public String getTaxi_ID() {return this.Taxi_ID;}
	public String getTrip_Start_Timestamp() {return this.Trip_Start_Timestamp;}
	public String getTrip_End_Timestamp() {return this.Trip_End_Timestamp;}
	public int getTrip_Seconds() {return this.Trip_Seconds;}
	public double getTrip_Miles() {return this.Trip_Miles;}
	public String getPickup_Census_Tract() {return this.Pickup_Census_Tract;}
	public String getDropoff_Census_Tract() {return this.Dropoff_Census_Tract;}
	public int getPickup_Community_Area() {return this.Pickup_Community_Area;}
	public int getDropoff_Community_Area() {return this.Dropoff_Community_Area;}
	public double getFare() {return this.Fare;}
	public double getTips() {return this.Tips;}
	public double getTolls() {return this.Tolls;}
	public double getExtras() {return this.Extras;}
	public double getTrip_Total() {return this.Trip_Total;}
	public String getPayment_Type() {return this.Payment_Type;}
	public String getCompany() {return this.Company;}
	public double getPickup_Centroid_Latitude() {return this.Pickup_Centroid_Latitude;}
	public double getPickup_Centroid_Longitude() {return this.Pickup_Centroid_Longitude;}
	public String getPickup_Centroid_Location() {return this.Pickup_Centroid_Location;}
	public String getDropoff_Centroid_Location() {return this.Dropoff_Centroid_Location;}
	public double getDropoff_Centroid_Latitude() {return this.Dropoff_Centroid_Latitude;}
	public double getDropoff_Centroid_Longitude() {return this.Dropoff_Centroid_Longitude;}

	
	/*=============================================================================
	 |   Method: public String set()
	 |			 public int set()	
	 |			 public double set()
	 |	We have three types which is String, int and double
	 | These are all 'Setters' for the data field values
	 *===========================================================================*/
	public void setTrip_ID(String Trip_ID) {this.Trip_ID = Trip_ID;}
	public void setTaxi_ID(String Taxi_ID) {this.Taxi_ID = Taxi_ID;}
	public void setTrip_Start_Timestamp(String Trip_Start_Timestamp) {this.Trip_Start_Timestamp = Trip_Start_Timestamp;}
	public void setTrip_End_Timestamp(String Trip_End_Timestamp) {this.Trip_End_Timestamp = Trip_End_Timestamp;}
	public void setTrip_Seconds(int Trip_Seconds) {this.Trip_Seconds = Trip_Seconds;}
	public void setTrip_Miles(double Trip_Miles) {this.Trip_Miles = Trip_Miles;}
	public void setPickup_Census_Tract(String Pickup_Census_Tract) {this.Pickup_Census_Tract = Pickup_Census_Tract;}
	public void setDropoff_Census_Tract(String Dropoff_Census_Tract) {this.Dropoff_Census_Tract = Dropoff_Census_Tract;}
	public void setPickup_Community_Area(int Pickup_Community_Area) {this.Pickup_Community_Area = Pickup_Community_Area;}
	public void setDropoff_Community_Area(int Dropoff_Community_Area) {this.Dropoff_Community_Area = Dropoff_Community_Area;}
	public void setFare(double Fare) {this.Fare = Fare;}
	public void setTips(double Tips) {this.Tips = Tips;}
	public void setTolls(double Tolls) {this.Tolls = Tolls;}
	public void setExtras(double Extras) {this.Extras = Extras;}
	public void setTrip_Total(double Trip_Total) {this.Trip_Total = Trip_Total;}
	public void setPayment_Type(String Payment_Type) {this.Payment_Type = Payment_Type;}
	public void setCompany(String Company) {this.Company = Company;}
	public void setPickup_Centroid_Latitude(double Pickup_Centroid_Latitude) {this.Pickup_Centroid_Latitude = Pickup_Centroid_Latitude;}
	public void setPickup_Centroid_Longitude(double Pickup_Centroid_Longitude) {this.Pickup_Centroid_Longitude = Pickup_Centroid_Longitude;}
	public void setPickup_Centroid_Location(String Pickup_Centroid_Location) {this.Pickup_Centroid_Location = Pickup_Centroid_Location;}
	public void setDropoff_Centroid_Location(String Dropoff_Centroid_Location) {this.Dropoff_Centroid_Location = Dropoff_Centroid_Location;}
	public void setDropoff_Centroid_Latitude(double Dropoff_Centroid_Latitude) {this.Dropoff_Centroid_Latitude = Dropoff_Centroid_Latitude;}
	public void setDropoff_Centroid_Longitude(double Dropoff_Centroid_Longitude) {this.Dropoff_Centroid_Longitude = Dropoff_Centroid_Longitude;}

	/*=============================================================================
	 |  Method: public String padString(String str, int size) {
	 |  Description: Given a size and a string you want to expand the string to needed size
	 |
	 *===========================================================================*/
	public String padString(String str, int size) {
		while (str.length() < size) {
			str += " ";
		}
		return str; //return a new string with some tabs

	}

	/*=============================================================================
	 * dumpObject(stream) -- write the content of the object's fields to the
	 * file represented by the given RandomAccessFile object reference.
	 * Primitive types (e.g., int) are written directly. Non-fixed-size values
	 * (e.g., strings) are converted to the maximum allowed size before being
	 * written. The result is a file of uniformly-sized records. Also note that
	 * text is written with just one byte per character, meaning that we are not
	 * supporting Unicode text.
	 *=============================================================================*/
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
			Tid.setLength(Trip_ID_Len); //if you have string then set the length to the length you needed
			stream.writeBytes(Tid.toString());
			Taid.setLength(Taxi_ID_Len);
			stream.writeBytes(Taid.toString());
			TST.setLength(Trip_Start_Timestamp_Len);
			stream.writeBytes(TST.toString());
			TET.setLength(Trip_End_Timestamp_Len);
			stream.writeBytes(TET.toString());

			stream.writeInt(Trip_Seconds);
			stream.writeDouble(Trip_Miles);

			PCensusT.setLength(Pickup_Census_Tract_Len);//same as above if your variable is string
			stream.writeBytes(PCensusT.toString());
			DCensusT.setLength(Dropoff_Census_Tract_Len);
			stream.writeBytes(DCensusT.toString());

			stream.writeInt(Pickup_Community_Area);//write int 
			stream.writeInt(Dropoff_Community_Area);

			stream.writeDouble(Fare); //write double
			stream.writeDouble(Tips);
			stream.writeDouble(Tolls);
			stream.writeDouble(Extras);
			stream.writeDouble(Trip_Total);

			PT.setLength(Payment_Type_Len);
			stream.writeBytes(PT.toString());
			C.setLength(Company_Len);
			stream.writeBytes(C.toString());

			stream.writeDouble(Pickup_Centroid_Latitude);
			stream.writeDouble(Pickup_Centroid_Longitude);
			PCentroidLo.setLength(Pickup_Centroid_Location_Len);
			stream.writeBytes(PCentroidLo.toString());

			stream.writeDouble(Dropoff_Centroid_Latitude);
			stream.writeDouble(Dropoff_Centroid_Longitude);
			DCentroidLo.setLength(Dropoff_Centroid_Location_Len);
			stream.writeBytes(DCentroidLo.toString());
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't write to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
	}

	/*=============================================================================
	 * fetchObject(stream) -- read the content of the object's fields from the
	 * file represented by the given RandomAccessFile object reference, starting
	 * at the current file position. Primitive types (e.g., int) are read
	 * directly. To create Strings containing the text, because the file records
	 * have text stored with one byte per character, we can read a text field
	 * into an array of bytes and use that array as a parameter to a String
	 * constructor.
	 *=============================================================================*/

	public void fetchObject(RandomAccessFile stream) {
		byte[] tripID = new byte[Trip_ID_Len]; // file -> byte[] -> String
		byte[] taxiID = new byte[Taxi_ID_Len]; // file -> byte[] -> String
		byte[] tst = new byte[Trip_Start_Timestamp_Len];
		byte[] tet = new byte[Trip_End_Timestamp_Len];
		byte[] pct = new byte[Pickup_Census_Tract_Len]; 
		byte[] dct = new byte[Dropoff_Census_Tract_Len]; 
		byte[] pt = new byte[Payment_Type_Len]; 
		byte[] co = new byte[Company_Len]; 
		byte[] pcl = new byte[Pickup_Centroid_Location_Len]; 
		byte[] dcl = new byte[Dropoff_Centroid_Location_Len]; 
																

		try {
			stream.readFully(tripID);
			Trip_ID = new String(tripID);
			stream.readFully(taxiID);
			Taxi_ID = new String(taxiID);
			stream.readFully(tst); //read fully for string variable
			Trip_Start_Timestamp = new String(tst);//transfer it to a new string
			stream.readFully(tet);
			Trip_End_Timestamp = new String(tet);

			Trip_Seconds = stream.readInt();//read int
			Trip_Miles = stream.readDouble();//read double

			stream.readFully(pct);
			Pickup_Census_Tract = new String(pct);
			stream.readFully(dct);
			Dropoff_Census_Tract = new String(dct);

			Pickup_Community_Area = stream.readInt();
			Dropoff_Community_Area = stream.readInt();

			Fare = stream.readDouble();
			Tips = stream.readDouble();
			Tolls = stream.readDouble();
			Extras = stream.readDouble();
			Trip_Total = stream.readDouble();

			stream.readFully(pt);
			Payment_Type = new String(pt);
			stream.readFully(co);
			Company = new String(co);

			Pickup_Centroid_Latitude = stream.readDouble();
			Pickup_Centroid_Longitude = stream.readDouble();
			stream.readFully(pcl);
			Pickup_Centroid_Location = new String(pcl);

			Dropoff_Centroid_Latitude = stream.readDouble();
			Dropoff_Centroid_Longitude = stream.readDouble();
			stream.readFully(dcl);
			Dropoff_Centroid_Location = new String(dcl);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
	}
	
	/*
	 *  Method: public int compareTo(DataRecord other) {
	 *  Description: sort according to trip id
	 */
	@Override
	public int compareTo(DataRecord other) {
		// TODO Auto-generated method stub
		return this.Trip_ID.compareTo(other.Trip_ID);
	}

}

/*
 * Main class for Prog1A. 
 * For an input file named file.csv, name the binary file file.bin. (That is, keep the file name, but
 * change the extension.) Don’t put a path on it; just create it in the current directory.)
 * Field types are limited to int, double, and String. All money fields are to be doubles, and pad Strings
 * on the right with spaces to reach the needed length(s). (For example, "abc ".)
 * For each column, all values must consume the same quantity of bytes. This is easy for numeric columns,
 * but for String columns we don’t want to waste storage. For those columns, you need to determine the
 * number of characters in the longest value, and use that to size each value in the column for storage. This
 * must be done for each execution of the program. (Why? The data doesn’t provide field sizes, so we need
 * to code defensively to accommodate unexpected changes.)
 */
public class Prog1A {
	/*
	 * Main method of the assignment Logic: Read file and transfer it to binary file first
	 * dump the line you read into binary file
	 */
	public static void main(String[] args) throws IOException {
		File fileRef; // used to create the file
		RandomAccessFile dataStream = null; // specializes the file I/O
		DataRecord curRecord = null; // the objects to write/read
		long numberOfRecords = 0; // loop counter for reading file
		ArrayList<DataRecord> records = new ArrayList<DataRecord>(); // store record

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

		fileRef = new File(textName.substring(0, (textName.length() - 3)) + "bin"); // replace csv to bin
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
			if (field[5].isEmpty()) {
				field[5] = "-1";
			}
			if (field[8].isEmpty()) {
				field[8] = "-1";
			}
			if (field[9].isEmpty()) {
				field[9] = "-1";
			}

			curRecord.setTrip_ID(field[0]);
			curRecord.setTaxi_ID(field[1]);
			curRecord.setTrip_Start_Timestamp(field[2]);
			curRecord.setTrip_End_Timestamp(field[3]);
			curRecord.setTrip_Seconds(Integer.parseInt(field[4]));
			curRecord.setTrip_Miles(Double.parseDouble(field[5]));
			curRecord.setPickup_Census_Tract(field[6]);
			curRecord.setDropoff_Census_Tract(field[7]);
			curRecord.setPickup_Community_Area(Integer.parseInt(field[8]));
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

			curRecord.setFare(Double.parseDouble(field[10])); //transfer double to string
			curRecord.setTips(Double.parseDouble(field[11]));
			curRecord.setTolls(Double.parseDouble(field[12]));
			curRecord.setExtras(Double.parseDouble(field[13]));
			curRecord.setTrip_Total(Double.parseDouble(field[14]));

			curRecord.setPayment_Type(field[15]);
			curRecord.setCompany(field[16]);
			
			//check if these field are missing
			if (field[17].isEmpty()) {
				field[17] = "-1";
			}
			if (field[18].isEmpty()) {
				field[18] = "-1";
			}
			if (field[20].isEmpty()) {
				field[20] = "-1";
			}
			if (field[21].isEmpty()) {
				field[21] = "-1";
			}

			curRecord.setPickup_Centroid_Latitude(Double.parseDouble(field[17]));
			curRecord.setPickup_Centroid_Longitude(Double.parseDouble(field[18]));
			curRecord.setPickup_Centroid_Location(field[19]);

			curRecord.setDropoff_Centroid_Latitude(Double.parseDouble(field[20]));
			curRecord.setDropoff_Centroid_Longitude(Double.parseDouble(field[21]));
			curRecord.setDropoff_Centroid_Location(field[22]);
			records.add(curRecord);
		}
		reader.close(); //end reading

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

		 //print record
		 System.out.println("\nThere are " + numberOfRecords
		 + " records in the file.\n");
		
//		 while (numberOfRecords > 0) {
//		 rec1.fetchObject(dataStream);
//		 System.out.println("Read state code of " + rec1.getStateCode());
//		 System.out.println("Read place code of " + rec1.getPlaceCode());
//		 System.out.println("Read county name of " + rec1.getCountyName());
//		 System.out.println();
//		 numberOfRecords--;
//		 }

		// Clean-up by closing the file

		try {
			dataStream.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
	}
}
