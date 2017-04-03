import java.io.*;
import java.sql.*; // For access to the SQL interaction methods
import java.util.Scanner;

public class prog3 {

	// primary key (School_EntityID)

	public static void main(String[] args) throws SQLException, IOException {

		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = null, // Oracle DBMS username
				password = null; // Oracle DBMS password

		if (args.length == 2) { // get username/password from cmd line args
			username = args[0];
			password = args[1];
		} else {
			System.out.println("\nUsage:  java JDBC <username> <password>\n"
					+ "    where <username> is your Oracle DBMS" + " username,\n    and <password> is your Oracle"
					+ " password (not your system password).\n");
			System.exit(-1);
		}

		// load the (Oracle) JDBC driver by initializing its base
		// class, 'oracle.jdbc.OracleDriver'.
		try {
			Class.forName("oracle.jdbc.OracleDriver");

		} catch (ClassNotFoundException e) {
			System.err.println("*** ClassNotFoundException:  " + "Error loading Oracle JDBC driver.  \n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);

		}

		// make and return a database connection to the user's
		// Oracle database

		Connection dbconn = null;
		try {
			dbconn = DriverManager.getConnection(oracleURL, username, password);
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

		// Send the query to the DBMS, and get and display the results
		Statement stmt = null;
		stmt = dbconn.createStatement();

		File inputFile = new File("/CSC460/csv/2010.csv");
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		int Fiscal_Year = 0;
		String State = "";// State varchar(7),
		String Country = "";// County varchar(7),
		int LEA_EntityID = 0;// LEA_EntityID integer,
		int LEA_CTDSnum = 0;// LEA_CTDSnum integer,
		String LEA_Name = "";// LEA_Name varchar(80),
		int School_EntityID = 0;// School_EntityID integer,
		int School_CTDSnum = 0;// School_CTDSnum integer,
		String School_Name = "";// School_Name varchar(69),
		String S_charter = "";// S_charter varchar(1),
		int Math_mean = 0;// Math_mean integer,
		int Math_PercFallsFarBelow= 0;// Math_PercFallsFarBelow integer,
		int Math_PercApproaches= 0;// Math_PercApproaches integer,
		int Math_PercMeets= 0;// Math_PercMeets integer,
		int Math_PercExceeds = 0;// Math_PercExceeds integer,
		int Math_PercPassing= 0;// Math_PercPassing integer,
		int Reading_mean= 0;// Reading_mean integer,
		int Reading_PercFallsFarBelow= 0;// Reading_PercFallsFarBelow integer,
		int Reading_PercApproaches= 0;// Reading_PercApproaches integer,
		int Reading_PercMeets= 0;// Reading_PercMeets integer,
		int Reading_PercExceeds= 0;// Reading_PercExceeds integer,
		int Reading_PercPassing= 0;// Reading_PercPassing integer,
		int Writing_mean= 0;// Writing_mean integer,
		int Writing_PercFallsFarBelow= 0;// Writing_PercFallsFarBelow integer,
		int Writing_PercApproaches= 0;// Writing_PercApproaches integer,
		int Writing_PercMeets = 0;// Writing_PercMeets integer,
		int Writing_PercExceeds= 0;// Writing_PercExceeds integer,
		int Writing_PercPassing= 0;// Writing_PercPassing integer,
		int Science_mean= 0;// Science_mean integer,
		int Science_PercFallsFarBelow= 0;// Science_PercFallsFarBelow integer,
		int Science_PercApproaches= 0;// Science_PercApproaches integer,
		int Science_PercMeets= 0;// Science_PercMeets integer,
		int Science_PercExceeds= 0;// Science_PercExceeds integer,
		int Science_PercPassing= 0;// Science_PercPassing integer,
		String currentLine;
		while ((currentLine = reader.readLine()) != null) {
			String[] field = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 34);
			for (int i = 0; i < 34; i++) {
				Fiscal_Year = Integer.parseInt(field[0]);
				State = field[1];
				Country = field[2];
				LEA_EntityID= Integer.parseInt(field[3]);
				LEA_CTDSnum = Integer.parseInt(field[4]);
				LEA_Name = field[5];
				School_EntityID = Integer.parseInt(field[6]);
				School_CTDSnum = Integer.parseInt(field[7]);
				School_Name = field[8];// School_Name varchar(69),
				S_charter = field[9];// S_charter varchar(1),
				Math_mean = Integer.parseInt(field[10]);
				Math_PercFallsFarBelow = Integer.parseInt(field[11]);
				Math_PercApproaches = Integer.parseInt(field[12]);
				Math_PercMeets = Integer.parseInt(field[13]);
				Math_PercExceeds = Integer.parseInt(field[14]);
				Math_PercPassing = Integer.parseInt(field[15]);
				Reading_mean = Integer.parseInt(field[16]);
				Reading_PercFallsFarBelow = Integer.parseInt(field[17]);
				Reading_PercApproaches = Integer.parseInt(field[18]);
				Reading_PercMeets = Integer.parseInt(field[19]);
				Reading_PercExceeds = Integer.parseInt(field[20]);
				Reading_PercPassing = Integer.parseInt(field[21]);
				Writing_mean = Integer.parseInt(field[22]);
				Writing_PercFallsFarBelow = Integer.parseInt(field[23]);
				Writing_PercApproaches = Integer.parseInt(field[24]);
				Writing_PercMeets = Integer.parseInt(field[25]);
				Writing_PercExceeds = Integer.parseInt(field[26]);
				Writing_PercPassing = Integer.parseInt(field[27]);
				Science_mean = Integer.parseInt(field[28]);
				Science_PercFallsFarBelow = Integer.parseInt(field[29]);
				Science_PercApproaches = Integer.parseInt(field[30]);
				Science_PercMeets = Integer.parseInt(field[31]);
				Science_PercExceeds = Integer.parseInt(field[32]);
				Science_PercPassing = Integer.parseInt(field[33]);
			}
			String query = "INSERT INTO AIMS2010 VALUES ("+Fiscal_Year+ State+ Country+LEA_EntityID+LEA_CTDSnum+
					LEA_Name+School_EntityID+School_CTDSnum+School_Name+S_charter+Math_mean+
					Math_PercFallsFarBelow+Math_PercApproaches+Math_PercMeets+Math_PercExceeds+
					Math_PercPassing+Reading_mean+Reading_PercFallsFarBelow+Reading_PercApproaches+
					Reading_PercMeets+Reading_PercExceeds+Reading_PercPassing+Writing_mean+Writing_PercFallsFarBelow+
					Writing_PercApproaches+Writing_PercMeets+Writing_PercExceeds+Writing_PercPassing+
					Science_mean+Science_PercFallsFarBelow+Science_PercApproaches+Science_PercMeets+
					Science_PercExceeds+Science_PercPassing+")";
			stmt.executeQuery(query);
		}
		System.out.println("import success");

	}
}