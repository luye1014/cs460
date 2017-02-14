import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
/*=============================================================================
|   Assignment: Program # 2
|    File Name: Prog2.java
|       Author: Lu Ye
|
|       Course: CSc 460 
|   Instructor: L. McCann
| Sect. Leader: Yawen Chen and Jacob Combs
|     Due Date: Feb 15 th, 2017, at the beginning of class
|
|     Language:  Java
|     Packages:  java.io
|
+-----------------------------------------------------------------------------
|	Description: Prog2 
|		In this program assignment, we created a dynamic hash index for the binary
|		file of taxi trips that we created in partA.java. And use it to satisfy a simple 
|		type of query. In particular we are to index on the trip total, using the digits in reversed 
| 		that is right to left order. Ignoring the decimal points and the dollar signs.
|
|	Techniques:  The programs steps are as follows:
|   	create a dynamic hash index for bin file
|		use it to satisfy a simple type of query
|		index on the trip total field 
|		using the digits in reversed order to query
|
|   Known Bugs:  None
|
*===========================================================================*/

/**
 * This is class for create a bucket to save indexed value, which the data of
 * the value we get from the partA bin file
 * 
 * @author Lu Ye
 *
 */
class bucket {
	private int file_offset; // bucket file offset in bucket
	private bucket sub[]; // sub node
	private int num;// slots used in offset[]
	static private int all_offset = 0; // hashfilelen for now
	// this is enough to deal withchicagotaxi-nov2016.bin
	public static final int BUCKETSIZE = 7000; 
	// constructor

	public bucket() {
		num = 0;
		file_offset = -1;
	}

	/**
	 * private static void alloc_bucketinfile(int file_off, RandomAccessFile dataStream) throws IOException 
	 * fill this bucket to file,just to allocate space in hashfile
	 * @param file_off
	 * @param dataStream
	 * @throws IOException
	 */
	private static void alloc_bucketinfile(int file_off, RandomAccessFile dataStream) throws IOException {
		dataStream.seek(file_off);
		byte tmp[] = new byte[BUCKETSIZE * 4]; // each int is 4 byte size
		dataStream.write(tmp); // just to fill the location
	}
	
	/**
	 * public static boolean insert(bucket root, int Recordoffset, String s, RandomAccessFile dataStream)
	 * This is the method for inserting data to the datastream, which makes data in a dynamic hash
	 * structure.
	 * @param root
	 * @param Recordoffset
	 * @param s
	 * @param dataStream
	 * @return
	 * @throws IOException
	 */
	public static boolean insert(bucket root, int Recordoffset, String s, RandomAccessFile dataStream)
			throws IOException {
		bucket tmp = root;//initialize the root of bucket
		int k;
		for (int i = s.length() - 1; i >= 0; i--) { //store the digit in a reversed order
			k = s.charAt(i);
			if (k == '.') // skip dot char
				continue;
			k -= '0'; //change the char array to int
			if (tmp.sub == null) {
				tmp.sub = new bucket[10];// set the first ten buckets
			}
			if (tmp.sub[k] == null) {
				tmp.sub[k] = new bucket();//create the bucket in the first ten buckets
			}
			tmp = tmp.sub[k];
		}
		if (tmp.num == 0) {//if the node is empty
			alloc_bucketinfile(all_offset, dataStream); //allocate the offset in the datastream
			tmp.file_offset = all_offset;//add the offset into file
			all_offset += BUCKETSIZE * 4;
		}
		if (tmp.num < BUCKETSIZE) { //the num of this structure should be smaller than bucket size
			dataStream.seek(tmp.file_offset + tmp.num * 4);
			dataStream.writeInt(Recordoffset);
			tmp.num++;
			return true;
		} else // overflow otherwise!!
		{
			System.out.println("bucket overflow at " + s);
			return false;
		}
	}
	
	/**
	 * private static int TraverseTree(String s, bucket root, RandomAccessFile hashfileStream, RandomAccessFile dbStrema,
	 * boolean hasLeading)
	 * This method is a help method for querying to traversal Dynamic hash tree and fetch data.
	 * @param s
	 * @param root
	 * @param hashfileStream
	 * @param dbStrema
	 * @param hasLeading
	 * @return
	 * @throws IOException
	 */
	private static int TraverseTree(String s, bucket root, RandomAccessFile hashfileStream, RandomAccessFile dbStream,
			boolean hasLeading) throws IOException {
		int counter = 0; 	//return the final number you are querying for 
		int ret = root.num;
		if (root.file_offset != -1) {
			int off[] = new int[BUCKETSIZE];
			hashfileStream.seek(root.file_offset);
			for (int i = 0; i < root.num; i++) {
				off[i] = hashfileStream.readInt();
			}

			DataRecord X = new DataRecord();
			for (int i = 0; i < root.num; i++) {
				dbStream.seek(off[i]);
				X.fetchObject(dbStream);

				NumberFormat formatter = new DecimalFormat("#0.00");
				String resultTotal = formatter.format(X.getTrip_Total());
				// System.out.print("=======>" + s + "\n");
				// System.out.print("=======>" + resultTotal + "\n");
				// System.out.println(hasLeading+"\n");
				if (hasLeading == true) {
					String temp = resultTotal.replaceAll("\\.", "");
					if (temp.length() == s.length()) {
						// System.out.print("resultto" + resultTotal + "\n");
						System.out.println(X.getTrip_ID() + " " + resultTotal);
						counter++;
					}
					// System.out.println(X.getTrip_ID() + " " + resultTotal);
				} else {
					System.out.println(X.getTrip_ID() + " " + resultTotal);
					counter++;
				}
			}
			return counter;
		}
		//if the sub node is not null
		if (root.sub != null) {
			for (int i = 0; i < 10; i++) {
				if (root.sub[i] != null) {
					ret += TraverseTree(s, root.sub[i], hashfileStream, dbStream, hasLeading);
				}
			}
		}
		return ret;

	}

	// return the total number
	/**
	 * public static int query(bucket root, String s, RandomAccessFile hashfileStream, RandomAccessFile dbStrema,
	 * boolean hasLeading)
	 * This is the method for querying your input
	 * @param root
	 * @param s
	 * @param hashfileStream
	 * @param dbStrema
	 * @param hasLeading
	 * @return
	 * @throws IOException
	 */
	public static int query(bucket root, String s, RandomAccessFile hashfileStream, RandomAccessFile dbStrema,
			boolean hasLeading) throws IOException {
		bucket tmp = root;
		for (int i = s.length() - 1; i >= 0; i--) {
			int k = s.charAt(i) - '0'; // change char to int
			if (tmp.sub == null) {
				return 0;
			}
			if (tmp.sub[k] == null) {
				return 0;
			}
			tmp = tmp.sub[k];
		}
		//traversal the tree to find the target you are looking for
		return TraverseTree(s, tmp, hashfileStream, dbStrema, hasLeading);
	}
}

/**
 * public class Prog2 
 * This is the main method for querying your input from keyboard and call method from 
 * class bucket and to traversal tree to find what you are looking for.
 * @author Lu Ye
 *
 */
public class Prog2 {
	/**
	 * private static boolean valid(String X) This is the method for checking
	 * whether the input is digit or not return true if it is a valid input
	 * otherwise return false
	 * 
	 * @param X
	 * @return
	 */
	private static boolean valid(String X) {
		int i;
		char ch;
		for (i = 0; i < X.length(); i++) {
			ch = X.charAt(i);
			//your input must be number
			if ((ch < '0') || (ch > '9'))
				return false;
		}
		return true;
	}
	
	/**
	 * Main method for the program.
	 * for querying your input from keyboard and call method from 
	 * class bucket and to traversal tree to find what you are looking for.
	 * @param args
	 */
	public static void main(String[] args) {
		RandomAccessFile dbbin = null; // DBbinfile
		RandomAccessFile hashfile = null; // DBbinfile
		String prefix = ""; //the target you are looking for
		int i, k = 0;
		DataRecord X = new DataRecord();
		long numberOfRecords = 0;
		bucket root = new bucket();
		String total;
		int query_num = 0;
		Scanner keyboard = new Scanner(System.in);//scan your input from keyboard
		boolean hasLeading = false;
		try {
			dbbin = new RandomAccessFile(args[0], "r");
		} catch (FileNotFoundException e) {
			System.out
			.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}

		try {
			hashfile = new RandomAccessFile("hashfile", "rw");
		} catch (FileNotFoundException e) { //file not found
			System.out
			.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}

		try {
			dbbin.seek(0);
		} catch (IOException e) {
			System.out.println(
					"I/O ERROR: Seems we can't reset the file " + args[0] + "pointer to the start of the file.");
			System.exit(-1);
		}

		/*
		 * Read the records and insert to root.
		 */

		try {
			numberOfRecords = dbbin.length() / DataRecord.RECORD_LENGTH;
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't get the file's length.");
			System.exit(-1);
		}

		if (numberOfRecords == 0) {
			System.out.println("There is no record in this File. The program ends, try some other files.");
			System.exit(-1);
		}
		for (i = 0; i < numberOfRecords; i++) {
			X.fetchObject(dbbin);
			//format the trip total
			total = String.format("%1.2f", Math.abs(X.getTrip_Total()));

			// total = Double.toString(Math.abs(X.getTrip_Total()));
			try {
				bucket.insert(root, k, total, hashfile);
			} catch (IOException e) {
				System.out.println("Insert failed");
				System.exit(-1);
			}
			k += DataRecord.RECORD_LENGTH;
		}

		while (true) {
			System.out.print("please input one target you want to query and input 'quit' or 'q' to exit: ");
			prefix = keyboard.next();//target string
			prefix = prefix.trim();//trim space
			hasLeading = false;
			// if users input quit then quit the program
			if (prefix.equals("quit") || prefix.equals("q")) {
				break;
			}
			//considering leading and trailing zero
			if (prefix.length() > 3) {
				for (i = 0; i < prefix.length() - 3; i++) {
					if (prefix.charAt(i) != '0') {
						break;
					}
				}
				if (i != 0) {
					hasLeading = true;
				}
				prefix = prefix.substring(i);
			}
			//check if your target is valid
			if (!valid(prefix)) {
				System.out.println("invalid input, only digits are allowed!\n");
				continue;
			}

			try {
				query_num = bucket.query(root, prefix, hashfile, dbbin, hasLeading);
			} catch (IOException e) {
				System.out.println("query failed");
				System.exit(-1);
			}
			// print query number
			System.out.println("\n" + query_num + " records matched your query.\n");
		}

	}
}
