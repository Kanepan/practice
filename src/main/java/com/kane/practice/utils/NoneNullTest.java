package com.kane.practice.utils;

import com.kane.practice.newfeature.lambda.LambdaTest;
import lombok.NonNull;


public class NoneNullTest {

    public static void main(String[] args) {
        NoneNullTest noneNullTest = new NoneNullTest();
        try {
            noneNullTest.NoneNullTest(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void NoneNullTest(@NonNull LambdaTest.Person person) {

        person.setAge(1);
    }
}
