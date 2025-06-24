package com.kane.test;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * App
 * author:  Andy.Z
 * date:    2023-05-07
 * description:  This program calculates the fibonacci number of a given number.
 *              The program will prompt the user to input a number, and then
 *             calculate the fibonacci number.
 *             The program will also measure the duration of the calculation.
 */
public class App {
    static long nth = 0;
    public static void main(String[] args) throws Exception {
        
        //print input prompt
        System.out.println("Please input a number to get the fibonacci number :");
       
        //get input from user
        Scanner input = new Scanner(System.in);
        try {            
            //get input from user
            nth = input.nextInt();
            //check if input is valid
            if (nth < 0) {
                System.out.println("Invalid input. Please input a positive number.");
                return;
            }        
        } catch (Exception e) {            
            System.out.println("Invalid input. Please input a number.");
            return;
        } finally {
            input.close();
        }
                
        long duration = measureFunctionDuration(() -> {
            // Code to be measured goes here            
            System.out.println("Fibonacci of " + nth +":\n"+ fibonacci(BigInteger.valueOf(nth)));
        });
    
        System.out.println("Function duration: " + formatNumber(duration) + " nanoseconds");        
    }

    //measure function duration
    public static long measureFunctionDuration(Runnable function) {
        long startTime = System.nanoTime();
        function.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }     
    
    //calculate fibonacci number
    public static BigInteger fibonacci(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        } else if (n.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        } else {
            BigInteger prev1 = BigInteger.ZERO;
            BigInteger prev2 = BigInteger.ONE;
            BigInteger current = BigInteger.ZERO;
            for (BigInteger i = BigInteger.valueOf(2); i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
                current = prev1.add(prev2);
                prev1 = prev2;
                prev2 = current;
            }
            return current;
        }
    }

    //format number
    public static String formatNumber(Long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(number);
        return formattedNumber;
    }    
}
