/*
 * Erick Cabrera
 * Sept. 22, 2018
 * AccountHolder.java
 * Lab 01
 * 
 * Purpose: This program is a simple bank application 
 * that calculates a user's yearly balance with applied
 * interest and allows them to both modify the interest
 * they want to earn and also the balance of their account
 * by deposits and withdrawals.
 */

public class AccountHolder {
    private double balance;
    private static double annualInterestRate;
    public AccountHolder(double balance){
    	//error trap for initial balance
        if(balance < 0)
            throw new IllegalArgumentException("You cannot start with a negative balance.");
        //set initial balance
        this.balance = balance;
    }
    //method to call interest rate
    public static double getAPR() {
        return annualInterestRate;
    }
    //method for depositing
    public void deposit(double amount){
    	//error trap negative deposits
        if(amount < 0)
            throw new IllegalArgumentException("You cannot deposit a negative amount to your account.");
        //increase balance by deposit
        balance += amount;
    }
    //method for withdrawals
    public void withdrawal(double amount){
    	//error trap negative withdrawals
        if(amount < 0)
            throw new IllegalArgumentException("You cannot withdraw a negative amount from your account.");
        //decrease balance by withdrawals
        double newBalance = balance - amount;
        //error trap if account will go below 100
        if(newBalance < 100)
            throw new IllegalArgumentException("This withdrawal will cause your account to drop below $100, and you are not allowed to do that.");
        //if balance is over 500 after withdrawal, no fee
        if(balance >= 500){
            if(newBalance >= 500){
                balance = newBalance;
                return;
            }
            //if account goes below 500, charge $50
            if(newBalance >= 100 && newBalance < 500){
            	System.out.println("Your account is going below $500. You will be charged a one time $50 fee.");
                //error trap to not let user go below 100 after $50 fee
                if(newBalance - 50 < 100) {
                    throw new IllegalArgumentException("This withdrawal will cause your account to drop below $100, " + "\nbecause a $50 one time fee is applied " +
                            						   "when your account drops below $500. " + "\nYou are not allowed to drop below $100.");
                }
                balance = newBalance - 50;
                return;
            }
        }
    }
    //method that increases balance by monthly rate
    public void monthlyInterest(){
        balance += balance * (annualInterestRate / 12.0);
    }
    //method that sets new interest rate
    public static void modifyMonthlyInterest(double interest){
    	//error trap if user does not set rate between 0 and 1
        if(interest < 0 || interest > 1)
            throw new IllegalArgumentException("Please provide a valid monthly interest rate, between %0 and %1.");
        //set the new interest rate
        annualInterestRate = interest;
    }

    @Override
    public String toString(){
        return String.format("$%.2f", balance);
    }
}
