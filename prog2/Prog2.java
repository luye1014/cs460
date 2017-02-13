import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

class bucket {
	private int file_offset; // bucket file offset in bucket
	private bucket sub[]; // sub node
	private int num;// slots used in offset[]
	static private int all_offset = 0; // hashfilelen for now
	public static final int BUCKETSIZE = 7000; // this is enough to deal with chicagotaxi-nov2016.bin
	//constructor 
	public bucket() {
		num = 0;
		file_offset = -1;
	}

	// fill this bucket to file,just to allocate space in hashfile
	private static void alloc_bucketinfile(int file_off, RandomAccessFile dataStream) throws IOException {
		dataStream.seek(file_off);
		byte tmp[] = new byte[BUCKETSIZE * 4];
		dataStream.write(tmp); // just to fill the location
	}

	public static boolean insert(bucket root, int Recordoffset, String s, RandomAccessFile dataStream)
			throws IOException {
		bucket tmp = root;
		int k;
		for (int i = s.length() - 1; i >= 0; i--) {
			k = s.charAt(i);
			if (k == '.') //skip dot char
				continue;
			k -= '0';
			if (tmp.sub == null) {
				tmp.sub = new bucket[10];
			}
			if (tmp.sub[k] == null) {
				tmp.sub[k] = new bucket();
			}
			tmp = tmp.sub[k];
		}
		if (tmp.num == 0) {
			alloc_bucketinfile(all_offset, dataStream);
			tmp.file_offset = all_offset;
			all_offset += BUCKETSIZE * 4;
		}
		if (tmp.num < BUCKETSIZE) {
			dataStream.seek(tmp.file_offset + tmp.num * 4);
			dataStream.writeInt(Recordoffset);
			tmp.num++;
			return true;
		} else // overflow!!
		{
			System.out.println("bucket overflow at " + s);
			return false;
		}
	}

	private static int TraverseTree(String s, bucket root, RandomAccessFile hashfileStream, RandomAccessFile dbStrema, boolean hasLeading)
			throws IOException {
		int counter = 0;
		int ret = root.num;
		if (root.file_offset != -1) {
			int off[] = new int[BUCKETSIZE];
			hashfileStream.seek(root.file_offset);
			for (int i = 0; i < root.num; i++) {
				off[i] = hashfileStream.readInt();
			}

			DataRecord X = new DataRecord();
			for (int i = 0; i < root.num; i++) {
				dbStrema.seek(off[i]);
				X.fetchObject(dbStrema);

				NumberFormat formatter = new DecimalFormat("#0.00");
				String resultTotal = formatter.format(X.getTrip_Total());
//				System.out.print("=======>" + s + "\n");
//				System.out.print("=======>" + resultTotal + "\n");
//				System.out.println(hasLeading+"\n");
				if(hasLeading == true){
					String temp = resultTotal.replaceAll("\\.", "");
					if(temp.length() == s.length()){
						//System.out.print("resultto" + resultTotal + "\n");
						System.out.println(X.getTrip_ID() + " " +  resultTotal);
						counter++;
					}
					//System.out.println(X.getTrip_ID() + " " +  resultTotal);
				}else{
					System.out.println(X.getTrip_ID() + " " +  resultTotal);
					counter++;
				}
			}
			return counter;
		}

		if (root.sub != null) {
			for (int i = 0; i < 10; i++) {
				if (root.sub[i] != null) {
					ret += TraverseTree(s, root.sub[i], hashfileStream, dbStrema, hasLeading);
				}
			}
		}
		return ret;

	}

	// return the total number
	public static int query(bucket root, String s, RandomAccessFile hashfileStream, RandomAccessFile dbStrema, boolean hasLeading)
			throws IOException {

		bucket tmp = root;
		for (int i = s.length() - 1; i >= 0; i--) {
			int k = s.charAt(i) - '0'; //change char to int
			if (tmp.sub == null) {
				return 0;
			}
			if (tmp.sub[k] == null) {
				return 0;
			}
			tmp = tmp.sub[k];
		}

		return TraverseTree(s, tmp, hashfileStream, dbStrema, hasLeading);

	}
};

public class Prog2 {
	/**
	 * private static boolean valid(String X) 
	 * This is the method for checking whether the input is digit or not
	 * return true if it is a valid input otherwise return false
	 * @param X
	 * @return
	 */
	private static boolean valid(String X) {
		int i;
		char ch;
		for (i = 0; i < X.length(); i++) {
			ch = X.charAt(i);
			if ((ch < '0') || (ch > '9'))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		RandomAccessFile dbbin = null; // DBbinfile
		RandomAccessFile hashfile = null; // DBbinfile
		String prefix = "";
		int i, k = 0;
		DataRecord X = new DataRecord();
		long numberOfRecords = 0;
		bucket root = new bucket();
		String total;
		int query_num = 0;
		Scanner keyboard = new Scanner(System.in);
		boolean hasLeading = false;
		try {
			dbbin = new RandomAccessFile(args[0], "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			hashfile = new RandomAccessFile("hashfile", "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

			total = String.format("%1.2f",Math.abs(X.getTrip_Total()));
			
			//total = Double.toString(Math.abs(X.getTrip_Total()));
			try {
				bucket.insert(root, k, total, hashfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			k += DataRecord.RECORD_LENGTH;
		}

		while (true) {
			System.out.print("please input one prefix to query,input 'quit' or 'q' to exit: ");
			prefix = keyboard.next();
			prefix = prefix.trim();
			hasLeading = false;
			if (prefix.equals("quit")||prefix.equals("q")) //if users input quit then quit the program
			{
				break;
			}
			
			if(prefix.length()>3){
				for(i = 0; i < prefix.length()-3; i++){
					if(prefix.charAt(i)!= '0'){
						break;
					}
				}
				if(i!=0){
					hasLeading = true;
				}
				prefix = prefix.substring(i);
			}
//			for (i = 0; i < prefix.length(); i++)// delete leading '0'
//			{
//				if(prefix.charAt(0)=='0' && prefix.charAt(i) != '0'){
//					//if(i!=prefix.length()-1){
//						hasLeading = true;
//					//}
//				}
//				if (prefix.charAt(i) != '0')
//					break;
//			}
//			if (i != prefix.length() && prefix.length() > 3) // not all '0'
//			{
//				prefix = prefix.substring(i);
//				
//			}
//			if( i == prefix.length() && i > 3){
//				pre
//			}
			// if all '0's, just let it be

			if (!valid(prefix)) {
				System.out.println("invalid input, only digits are allowed!\n");
				continue;
			}

			try {
				query_num = bucket.query(root, prefix, hashfile, dbbin, hasLeading);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("\n" + query_num + " records matched your query.\n");
		}

	}
}
