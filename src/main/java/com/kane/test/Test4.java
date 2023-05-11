package com.kane.test;

public class Test4 {

    public static void main(String[] args) {
        //random  generate fibonacci number
        int n = 10; //generate 10 fibonacci numbers
//        for (int i = 1; i <= n; i++) {
//            System.out.println(generateFibonacciNumber(i));
//        }
        System.out.println(generateFibonacciNumber(8000));
    }

    public static Long  generateFibonacciNumber(int number ){
        if(number == 1 || number == 2){
            return 1L;
        }
        return generateFibonacciNumber(number - 1) + generateFibonacciNumber(number - 2);

    }
}
