/*
 * Erick Cabrera
 * Sept. 22, 2018
 * AccountHolderTest.java
 * Lab 01
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class AccountHolderTest {
    private static AccountHolder ah;
    private static Scanner sc;
    public static void main(String[] args) {
    	//set initial 4% rate
    	AccountHolder.modifyMonthlyInterest(0.04);
    	//Welcome message
    	System.out.println("Welcome to Bank of IIT.");
        double amount = -1;
        boolean quit = false;
        sc = new Scanner(System.in);
        //initial balance
        do{
            System.out.println("Enter an amount to open your account with: ");
            String initAmount = sc.nextLine();
            if(initAmount.isEmpty())
                continue;
            //error trap for initializing balance
            try{
                amount = Double.parseDouble(initAmount);
                ah =  new AccountHolder(amount);
            }catch (Exception ex){
                if(ex instanceof IllegalArgumentException)
                    System.out.println(ex.getMessage());
                else
                    System.out.println("Please enter a valid amount to start your account with:");
                continue;
            }
        }while(amount < 0);
        
        //loop that determines action to take while error trapping
        do {
            printMenu();
            int option = -1;
            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 1 || option > 7) continue;
            }catch(Exception ex){
                continue;
            }
            try {
                switch (option) {
                    case 1:
                        makeADeposit();
                        break;
                    case 2:
                        makeAWithdrawal();
                        break;
                    case 3:
                        applyMonthlyInterest();
                        break;
                    case 4:
                        System.out.println("Current Balance: " + ah);
                        break;
                    case 5:
                        changeInterestRate();
                        break;
                    case 6:
                        printYearlyInterest();
                        quit = true;
                        break;
                    default:
                        quit = true;
                        break;
                }
            }catch(IllegalArgumentException ill){
                System.out.println(ill.getMessage());
            }
        }while (!quit);
        sc.close();
        //time stamp at end of program
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("Cur dt=" + timeStamp + "\nProgrammed by Erick Cabrera\n");

    }

    //method to change interest rate
    private static void changeInterestRate() {
        boolean quit = false;
        do {
            try {
                System.out.println("Please enter a new interest rate:");
                double interest = Double.parseDouble(sc.nextLine());
                AccountHolder.modifyMonthlyInterest(interest);
                quit = true;
            } catch (NullPointerException ex) {
            } catch (NumberFormatException nfe) {
            }
        }while(!quit);
    }

    //method to make a deposit
    private static void makeADeposit() {
        boolean quit = false;
        do {
            try {
                System.out.println("Please enter a deposit amount:");
                double deposit = Double.parseDouble(sc.nextLine());
                ah.deposit(deposit);
                quit = true;
            } catch (NullPointerException ex) {
            } catch (NumberFormatException nfe) {
            }
        }while(!quit);

    }

    //method to make a withdrawal
    private static void makeAWithdrawal() {
        boolean quit = false;
        do {
            try {
                System.out.println("Please enter a withdrawal amount:");
                double withdrawl = Double.parseDouble(sc.nextLine());
                ah.withdrawal(withdrawl);
                quit = true;
            } catch (NullPointerException ex) {
            } catch (NumberFormatException nfe) {
            }
        }while(!quit);

    }

    //method that applied the monthly interest to account
    private static void applyMonthlyInterest() {
        ah.monthlyInterest();

    }

    //method to print out table with balances info
    private static void printYearlyInterest() {
        System.out.println(String.format("Monthly balances for one year at %.2f", AccountHolder.getAPR()));
        System.out.println("Balances:");
        System.out.println("Account Balance w. Interest");
        System.out.println(String.format("Base \t\t%s", ah));
        for (int i = 1; i < 13; i++){
            ah.monthlyInterest();
            System.out.println(String.format("Month %d:\t%s", i, ah));
        }
    }

    //menu showing user options
    private static void printMenu() {
        System.out.println("Enter one of the options:\n" +
                "1. Deposit\n" +
                "2. Withdraw\n" +
                "3. Apply Monthly Interest Once\n" +
                "4. Print Account Balance\n" +
                "5. Change Interest Rate\n" +
                "6. Print Yearly Balance With Applied Interest\n" +
                "7. Exit");
    }
}