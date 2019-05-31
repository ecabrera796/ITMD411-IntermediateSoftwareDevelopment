/*
 * Erick Cabrera
 * Nov. 19, 2018
 * Records.java
 * Lab 04
 */

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Records extends BankRecords {
    private static Records instance;
    private static List<BankRecords> femaleRecords = new ArrayList<>();
    private static List<BankRecords> maleRecords = new ArrayList<>();
    private FileWriter fileWriter;
    //default constructor
    private Records(){
        openRecords();
    }
    //method to open records
    private void openRecords(){
    	//check if records file is in folder
        try{
            if(fileWriter != null) return;
            fileWriter = new FileWriter("bankrecords.txt");
        }catch (IOException ioex){}
    }
    //access to public methods of the class
    public static Records getInstance(){
        if(instance == null){
            synchronized (BankRecords.class){
                instance = new Records();
            }
        }
        return instance;
    }
    //method to close records
    public void closeRecords(){
        if(fileWriter == null) return;
        try{
            //let user know how many records are in the data file
            printToConsoleAndFile(String.format("%s \t programmed by Erick Cabrera\n", LocalDateTime.now().format(BankRecords.dateFormat)));
            printToConsoleAndFile(String.format("Total records in bank-detail.csv are %d", BankRecords.array.size()));
            fileWriter.close();
        }catch (IOException ex){

        }finally {
            fileWriter = null;
        }
    }
    //method that reads in data from Records and displays it
    @Override
    public void readData(){
        super.readData();
        femaleRecords.clear();
        maleRecords.clear();
        BankRecords[] robjsArray = robjs.toArray(new BankRecords[0]);
        Arrays.sort(robjsArray, new GenderCompare());
        for (BankRecords record: robjsArray) {
            if(record.getSex().equalsIgnoreCase("male"))
                maleRecords.add(record);
            else
                femaleRecords.add(record);
        }
        printToConsoleAndFile("Data analytic results:\n\n");
        calculateAverageIncomes(maleRecords);
        calculateAverageIncomes(femaleRecords);
        printToConsoleAndFile("\n");
        findFemales();
        printToConsoleAndFile("\n");
        sortMales();
    }
    //method to sort data for males
    private void sortMales() {
        BankRecords[] malesSortedArray =  maleRecords.toArray(new BankRecords[0]);
        Arrays.sort(malesSortedArray, new RegionCompare());
        List<BankRecords> innerCity = new ArrayList<>();
        List<BankRecords> town = new ArrayList<>();
        List<BankRecords> rural = new ArrayList<>();
        List<BankRecords> suburban = new ArrayList<>();
        for (BankRecords record : malesSortedArray) {
            switch (record.getRegion().toLowerCase()){
                case "town":
                    town.add(record);
                    break;
                case "inner_city":
                    innerCity.add(record);
                    break;
                case "rural":
                    rural.add(record);
                    break;
                    default:
                        suburban.add(record);
                        break;
            }
        }
        findMales(innerCity, "Inner city");
        findMales(rural, "Rural");
        findMales(suburban, "Suburban");
        findMales(town, "Town");
    }
    //method that count how many males have a car and 1 child
    private void findMales(List<BankRecords> records, String region) {
        int count = 0;
        for (BankRecords record : records) {
            if(record.getCar().equalsIgnoreCase("yes") && record.getChildren() == 1)
                count++;
        }
        StringBuilder sb = new StringBuilder(region);
        sb.append(" region males with a car & one child: ");
        sb.append(count);
        sb.append("\n");
        printToConsoleAndFile(sb.toString());
    }
    //method to find females with mortgage and savings account
    private void findFemales() {
        int count = 0;
        for (BankRecords record : femaleRecords) {
            if(record.getMortgage().equalsIgnoreCase("yes") && record.getSave_act().equalsIgnoreCase("yes"))
                count++;
        }
        printToConsoleAndFile("Number of Females with a mortgage & savings account: "+count+"\n");
    }
    //method to calculate average income of males and females
    private void calculateAverageIncomes(List<BankRecords> records) {
        double totalIncome = 0;
        for (BankRecords record : records) {
            totalIncome += record.getIncome();
        }
        StringBuilder sb = new StringBuilder("Average income ");
        sb.append(records.get(0).getSex());
        sb.append("S: $");
        sb.append(String.format("%.2f\n", totalIncome/records.size()));
        printToConsoleAndFile(sb.toString());
    }
    //create formatted object to write output directly to the console and to a file
    private void printToConsoleAndFile(String s) {
        System.out.print(s);
        try {
            fileWriter.append(s);
        }catch (IOException ioex){}
    }
    //compare Gender
    public class GenderCompare implements Comparator<BankRecords> {
        @Override
        public int compare(BankRecords record1, BankRecords record2) {
            // use compareTo to compare strings
            int result = record1.getSex().compareTo(record2.getSex());
            return result;
        }
    }
    //compare Regions
    public class RegionCompare implements Comparator<BankRecords> {
        @Override
        public int compare(BankRecords record1, BankRecords record2) {
            // use compareTo to compare strings
            int result = record1.getRegion().compareTo(record2.getRegion());
            return result;
        }
    }
}
