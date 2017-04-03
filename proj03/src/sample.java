
/*===========================================================================================================================
 |   Assignment:  Program # -3
 |       Author: Wenjie,Zhang
 |
 |       Course:  CSc 460 
 |   Instructor:  L. McCann
 | Sect. Leader:  Zhenge,Zhao
 |     Due Date:  Nov 3, 2016, at the beginning of class
 |
 |     Language:  Java
 |     Packages:  java.io
 |
 +--------------------------------------------------------------------------------------------------------------------------
 |  Description:    Write an application in Java that offers the user a menu of three questions that can be asked of the
 |                  database of WRCT data. The questions are:
 |                  (a) For a percentage chosen by the user (one of “% Minimal,” “% Basic,” “% Proficient,” or “%
 |                      Advanced”), for each of the four school years, which five schools had the highest percentages?
 |                      Order the results in descending order of the percentage.
 |                  (b) For a given school name, show the “% Not Tested” for each of the years, and state, in words, if the
 |                      trend has been strictly increasing (that is, the percentage has always gone up year-to-year across
 |                      the available years), strictly decreasing (gone down year-to-year), or neither.
 |                  (c) A question of your choice.
 |
 *=========================================================================================================================*/


import java.io.*;
import java.sql.*; // For access to the SQL interaction methods
import java.util.Scanner;

public class sample {
    public static void main(String[] args) throws SQLException {

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

        System.out.println("Type q1, q2, q3 go to specific question : ");
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            switch (s.next()) {          
            case "q1":  
                System.out.println("Type 1, 2, 3, 4 to go to specific part of question1 : (or type exitQ1 to exit the q1");             
                while(s.hasNext()){
                    String tar = s.next();
                    if(tar.equals("1")){
                        Q1result(dbconn, "minNumPct");
                    }else if(tar.equals("2")){
                        Q1result(dbconn, "basicNumPct");
                    }else if(tar.equals("3")){
                        Q1result(dbconn, "profNumPct");
                    }else if(tar.equals("4")){
                        Q1result(dbconn, "advNumPct");
                    }else if(tar.equals("exitQ1")){
                        break;
                    }else{
                        System.out.println("Please enter a valid value(1,2,3,4) for q1 or type exitQ1 to exit q1!");
                    }
                }
            case "exitQ1":
                System.out.println("Please type q1, q2, q3 go to specific question : ");
                break;

            case "q2":
            System.out.println("Type one school name for the question2: (or type exitQ2 to exit the q2");
            while(s.hasNext()){
                String tar = s.nextLine();
                if(tar.equals("exitQ2")){
                    break;
                }else{
                    Q2result(dbconn, tar);
                }
            }
            case "exitQ2":
                System.out.println("Please type q1, q2, q3 go to specific question : ");
                break;
            
            case "q3":
            System.out.println("Type a school name for the question3: (or type exitQ3 to exit the q3");
            while(s.hasNext()){
                String target = s.nextLine();
                if(target.equals("exitQ3")){
                    break;
                }else{
                    Q3result(dbconn, target);
                }
            }
            default:
                System.out.println("Please type q1, q2, q3 go to specific question : ");
            }
        }
    }

    public static void Q3result(Connection dbconn, String name) throws SQLException {
        Statement stmt = null;
        ResultSet answer = null;
        String query3 = "SELECT table1.notTestPct as \"98-99year\", table2.notTestPct as \"00-01year\", table3.notTestPct as \"02-03year\", table4.notTestPct as \"04-05year\"\n"
                        + "FROM table1\n"
                            + "FULL OUTER JOIN table2\n" + "ON table1.schlNum=table2.schlNum and table1.distNum=table2.distNum\n"
                            + "FULL OUTER JOIN table3\n" + "ON table1.schlNum=table3.schlNum and table1.distNum=table3.distNum\n"
                            + "FULL OUTER JOIN table4\n" + "ON table1.schlNum=table4.schlNum and table1.distNum=table4.distNum\n"
                        + "WHERE table1.schName = '"+ name + "'";
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query3);
            if (answer != null) {
                System.out.printf("The results of the query3 are:\n");
            }
            // if(!answer.next()){
            //     System.out.println("We cannot find the output with the given school name.");
            // }
            // Use next() to advance cursor through the result
            // tuples and print their attribute values
            while (answer.next()) {
                // Get the data about the query result to learn
                // the attribute names and use them as column headers
                ResultSetMetaData answermetadata = answer.getMetaData();
                for (int a = 1; a <= answermetadata.getColumnCount(); a++) {
                    System.out.printf(answermetadata.getColumnName(a) + "\t");
                }
                System.out.println();
                System.out.printf(answer.getFloat("98-99year") + "\t\t" + answer.getFloat("00-01year")
                        + "\t\t" + answer.getFloat("02-03year") + "\t\t" + answer.getFloat("04-05year"));
                System.out.println("\nthe average of the four years are: "+(answer.getFloat("98-99year")+answer.getFloat("00-01year")+answer.getFloat("02-03year")+answer.getFloat("04-05year"))/4);
                
            }
        } catch (SQLException e) {
            System.err.println("*** SQLException:  " + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        }
    }





    public static void Q2result(Connection dbconn, String name) throws SQLException {
        Statement stmt = null;
        ResultSet answer = null;
        String query2 = "SELECT table1.notTestPct as \"98-99year\", table2.notTestPct as \"00-01year\", table3.notTestPct as \"02-03year\", table4.notTestPct as \"04-05year\"\n"
                        + "FROM table1\n"
                            + "FULL OUTER JOIN table2\n" + "ON table1.schlNum=table2.schlNum and table1.distNum=table2.distNum\n"
                            + "FULL OUTER JOIN table3\n" + "ON table1.schlNum=table3.schlNum and table1.distNum=table3.distNum\n"
                            + "FULL OUTER JOIN table4\n" + "ON table1.schlNum=table4.schlNum and table1.distNum=table4.distNum\n"
                        + "WHERE table1.schName = '"+ name + "'";
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query2);
            if (answer != null) {
                System.out.printf("The results of the query2 are:\n");

                // if(!answer.next()){
                //     System.out.println("We cannot find the output with the given school name.\n");
                // }

                // Use next() to advance cursor through the result
                // tuples and print their attribute values
                while (answer.next()) {
                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers
                    ResultSetMetaData answermetadata = answer.getMetaData();
                    for (int a = 1; a <= answermetadata.getColumnCount(); a++) {
                        System.out.printf(answermetadata.getColumnName(a) + "\t");
                    }
                    System.out.println();
                    System.out.printf(answer.getFloat("98-99year") + "\t\t" + answer.getFloat("00-01year")
                            + "\t\t" + answer.getFloat("02-03year") + "\t\t" + answer.getFloat("04-05year"));
                    if(answer.getFloat("98-99year")>answer.getFloat("00-01year") && answer.getFloat("00-01year")>answer.getFloat("02-03year")&&answer.getFloat("02-03year")>answer.getFloat("04-05year")){
                        System.out.println("\ndecreasing trend");
                    }else if (answer.getFloat("98-99year")<answer.getFloat("00-01year") && answer.getFloat("00-01year")<answer.getFloat("02-03year")&&answer.getFloat("02-03year")<answer.getFloat("04-05year")){
                        System.out.println("\nincreasing trend");
                    }else{
                         System.out.println("\nno trend");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("*** SQLException:  " + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        }
    }






    public static void Q1result(Connection dbconn, String str) throws SQLException {
        String query[] = new String[4];
        query[0]="SELECT schName, "+ str + " FROM wenjiezhang.table1 WHERE "+str+">0 order by " + str + " desc";
        query[1]="SELECT schName, "+ str + " FROM wenjiezhang.table2 WHERE "+str+">0 order by " + str + " desc";
        query[2]="SELECT schName, "+ str + " FROM wenjiezhang.table3 WHERE "+str+">0 order by " + str + " desc";
        query[3]="SELECT schName, "+ str + " FROM wenjiezhang.table4 WHERE "+str+">0 order by " + str + " desc";
        for(int i = 0; i<4;i++){
            Statement stmt = null;
            ResultSet answer = null;
            try {
                stmt = dbconn.createStatement();
                answer = stmt.executeQuery(query[i]);
                if (answer != null) {
                    System.out.printf("\nThe results of the query1 are:\n");

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                    ResultSetMetaData answermetadata = answer.getMetaData();
                    for (int a = 1; a <= answermetadata.getColumnCount(); a++) {
                        System.out.printf(answermetadata.getColumnName(a) + "\t\t\t\t");
                    }
                    System.out.println();

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values
                    int j = 0;
                    while (answer.next() && j < 5) {
                        System.out.printf("%-30s %10s",answer.getString("schName"),answer.getFloat(str));
                        System.out.println();
                        j++;
                    }
                }
                System.out.println();

            } catch (SQLException e) {
                System.err.println("*** SQLException:  " + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);
            }
        }
    }

}