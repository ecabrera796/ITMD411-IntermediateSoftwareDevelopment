/*
 * Erick Cabrera
 * Oct. 26, 2018
 * BankRecords.java
 * Lab 03
 * 
 * Purpose: The purpose of this program is
 * to take in a file of records and display them to a user
 * while also providing a bit of information about the
 * file and allowing the user to choose how many
 * records they wish to retrieve.
 */

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankRecords extends Client{
    //setup static objects for IO processing

    //array of BankRecords objects
    protected static List<BankRecords> robjs = new ArrayList<>();
    //array list to hold spreadsheet rows & columns
    static ArrayList<List<String>> array = new ArrayList<>();
    //format time stamps
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //initialize variables
    private String id;
    private int age;
    private String sex;
    private String region;
    private double income;
    private String married;
    private int children;
    private String car;
    private String save_act;
    private String current_act;
    private String mortgage;
    private String pep;
    //method to read file data
    @Override
    public void readData() {
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(new File("bank-detail.csv")));
            String line;
            System.out.println(String.format("CSV file import started at %s", LocalDateTime.now().format(dateFormat)));
            while((line = br.readLine()) != null){
                array.add(Arrays.asList(line.split(",")));
            }
        //error trap if file is missing
        }catch(FileNotFoundException fex){
            System.err.println("Failed to find bank-detail.csv");
        }catch (Exception ex){
        }finally{
            if(br != null){
                try {
                    br.close();
                }catch (IOException ioex){}
            }
            processData(); //move to next method
        }
    }
    //method that starts populating array list with data from file
    @Override
    public void processData() {
        robjs.clear();
        for (List<String> rowData : array) {
            BankRecords br = new BankRecords();
            try{
                br.setId(rowData.get(0));
                br.setAge(Integer.parseInt(rowData.get(1)));
                br.setSex(rowData.get(2));
                br.setRegion(rowData.get(3));
                br.setIncome(Double.parseDouble(rowData.get(4)));
                br.setMarried(rowData.get(5));
                br.setChildren(Integer.parseInt(rowData.get(6)));
                br.setCar(rowData.get(7));
                br.setSave_act(rowData.get(8));
                br.setCurrent_act(rowData.get(9));
                br.setMortgage(rowData.get(10));
                br.setPep(rowData.get(11));
            }catch(IndexOutOfBoundsException ex){}
            catch (NumberFormatException nex){}
            finally {
                robjs.add(br);
            }
        }
        //lets you know when the file finish importing
        System.out.println(String.format("CSV file import completed at %s", LocalDateTime.now().format(dateFormat)));
       // printData(); //move to next method
    }
    //method that prints the array list in the form of a nice table
    @Override
    public void printData() {
        String heading = String.format("%-8s\t%-4s\t%-7s\t%-11s\t%-12s\t%-8s\t%-9s\t%-4s\t%-9s\t%-12s" + 
        							   "\t%-9s\t%-4s", "ID", "AGE", "SEX", "REGION","INCOME", "MARRIED", 
        							   "CHILDREN", "CAR", "SAVE_ACT", "CURRENT_ACT","MORTGAGE", "PEP");
        for (int i = 0; i<robjs.size(); i++){
            if(robjs.get(i) == null) break; // hit last element in list
            if(i<25)
                System.out.println(heading);
            System.out.println(robjs.get(i));
        }
    }
    //method that prints the records the user wants
    public void printData(int recordsToPrint){
        if(recordsToPrint < 0) return;

        String heading = String.format("%-8s\t%-4s\t%-7s\t%-11s\t%-12s\t%-8s\t%-9s\t%-4s\t%-9s" +
        							   "\t%-12s\t%-9s\t%-4s", "ID", "AGE", "SEX", "REGION","INCOME",
        							   "MARRIED", "CHILDREN", "CAR", "SAVE_ACT", "CURRENT_ACT",
        							   "MORTGAGE", "PEP");
        System.out.println(heading);
        for (int i = 0; i<robjs.size() && i<recordsToPrint; i++){
            if(robjs.get(i) == null) break; // hit last element in list
            System.out.println(robjs.get(i));
        }
    }
    //method that formats the table to look nice
    @Override
    public String toString(){
        return String.format("%-8s\t%-4d\t%-7s\t%-11s\t%-12.2f\t%-8s\t%-9d\t%-4s\t%-9s\t%-12s" + 
        					 "\t%-9s\t%-4s", id, age, sex, region, income, married, children, car, 
        					 save_act, current_act, mortgage, pep);
    }
    //setters and getters for all categories in the data
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getSave_act() {
        return save_act;
    }

    public void setSave_act(String save_act) {
        this.save_act = save_act;
    }

    public String getCurrent_act() {
        return current_act;
    }

    public void setCurrent_act(String current_act) {
        this.current_act = current_act;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getPep() {
        return pep;
    }

    public void setPep(String pep) {
        this.pep = pep;
    }
}