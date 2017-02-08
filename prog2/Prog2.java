import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

class Entry {
	private int tripTotal;
	private int pointer;
	
	/**
	 * public int getTripTotal()
	 * The getter of tripTotal
	 * @return
	 */
	public int getTripTotal(){
		return tripTotal;
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
	public void setTripTotal(int tripTotal){
		this.tripTotal = tripTotal;
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
		try {
			stream.writeInt(tripTotal);
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
			tripTotal = stream.readInt();
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
		
		RandomAccessFile bucketsStream = null;
	}

}
