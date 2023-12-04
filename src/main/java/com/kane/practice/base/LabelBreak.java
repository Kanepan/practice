package com.kane.practice.base;

public class LabelBreak {

    public static void main(String[] args) {
        Label:
        for (int i = 0; i < 10; i++) {
            System.out.println("for1:" + i);
            for (int j = 0; j < 10; j++) {
                System.out.println("for2:" + j);
                if (j == 5) {
                    break Label ;
                }
            }
        }
    }
}
