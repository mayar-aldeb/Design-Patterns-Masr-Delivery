package org.route.validation;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputValidation {

    public static final Scanner SCANNER =new Scanner(System.in);

    public static String setValidString(String message){
        String name="";
        while (name.isBlank()){
            System.out.print(message);
            name= SCANNER.nextLine();
        }
        return name;
    }

    public static double setDouble(String message){
        System.out.print(message);
        double num;
        boolean flag;
        while (!(flag=SCANNER.hasNextDouble())||(num=SCANNER.nextDouble())<=0){
            System.out.print("enter a valid number :  ");
            if(!flag)
                SCANNER.next();
        }
        SCANNER.nextLine();
        return num;
    }

    public static BigDecimal setBigDecimal(String message){
        System.out.print(message);
        BigDecimal num;
        boolean flag;
        while (!(flag=SCANNER.hasNextBigDecimal())||
                (num=SCANNER.nextBigDecimal()).compareTo(BigDecimal.valueOf(0))<=0){
            System.out.print("enter a valid number :  ");
            if(!flag)
                SCANNER.next();
        }
        SCANNER.nextLine();
        return num;
    }

    public static int setInt(String message){
        System.out.print(message);
        int num;
        boolean flag;
        while (!(flag=SCANNER.hasNextInt())||(num=SCANNER.nextInt())<=0){
            System.out.print("enter a valid integer number :  ");
            if(!flag)
                SCANNER.next();
        }
        SCANNER.nextLine();
        return num;
    }

    public static int setFilterOption(String message){
        int choice=0;
        while (choice<1||choice>5){
            choice=InputValidation.setInt(message);
        }
        return choice;
    }

    public static int setItemOption(){
        System.out.println("""
                1- Add Item.
                2- Remove Item.
                3- Confirm Items.""");
        int choice=0;
        while (choice<1||choice>3){
            choice= InputValidation.setInt("enter your choice: ");
        }
        return choice;
    }


}
