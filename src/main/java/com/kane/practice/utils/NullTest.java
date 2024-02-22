package com.kane.practice.utils;
import com.kane.practice.base.genericity.TempBO;

import javax.annotation.Nonnull;
public class NullTest {


    public static void main(String[] args) {
        NullTest nullTest = new NullTest();
        TempBO.Person person = new TempBO.Person();
        try {
            nullTest.nonNullExample(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String name;

    public void nonNullExample(@Nonnull TempBO.Person person) {

        this.name = person.getName();
    }
}
