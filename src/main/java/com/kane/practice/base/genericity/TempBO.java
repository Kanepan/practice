package com.kane.practice.base.genericity;

import lombok.Data;

public class TempBO<T> {
    private T data;

    public TempBO(T t) {
        this.data = t;
    }

    public TempBO() {

    }

    public T getT() {
        return data;
    }

    @Data
    public static class Person {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        TempBO<Person> tempBO = new TempBO<>();
        //获取tempBO泛型的类型

    }

}
