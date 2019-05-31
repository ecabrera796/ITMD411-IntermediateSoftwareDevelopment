/*
 * Erick Cabrera
 * Oct. 26, 2018
 * Main.java
 * Lab 03
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Welcome to Bank of IIT Chicago Customer Record Generation Section");
        //read in data from Records
        Records.getInstance().readData();
        boolean cont = true;
        Scanner sc = new Scanner(System.in);
        //loop to ask user how much data to retrieve and actually retrieve it for them
        do {
            System.out.println("How many records do you want to print?\nEnter a value less than 1 to exit.");
            String line = sc.nextLine();
            //if no response, ask again
            if(line == null || line.isEmpty()) continue;
            try{
                int records = Integer.parseInt(line);
                //if they do not want to see any more data (0 data) close program
                if(records < 1){
                    cont=false;
                }else{
                	//print data from Records
                    Records.getInstance().printData(records);
                }
            }catch (Exception ex){
                continue;
            }
        }while (cont);
        sc.close();
        //let user know how many records is in the data file
        Records.getInstance().closeRecords();
        System.exit(0);
        //time stamp and name
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("Cur dt=" + timeStamp + "\nProgrammed by Erick Cabrera\n");
    }
}