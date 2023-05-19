/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.newfeature.optional;

import com.kane.common.domain.Result;

import java.util.Arrays;
import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) throws Exception {
        OptionalTest optionalTest = new OptionalTest();
        optionalTest.test6();
//        System.out.println("--------------------------");
//        optionalTest.test2();
//        System.out.println("--------------------------");
//        optionalTest.test3();
//        System.out.println("--------------------------");
//        optionalTest.test4();
    }

    private void test() {
        Person person = new Person("测试", 18);
        Optional<Person> personOpt = Optional.ofNullable(person);
        personOpt.ifPresent(p -> System.out.println(p.getAge()));
        // ifPresent  是 Consumer<? super T> consumer

        personOpt = Optional.ofNullable(null);
        Person person2 = personOpt.orElse(new Person("测试other", 19));
        System.out.println(person2.getName());
    }


    private void test2() {
        Person person = new Person("测试", 18);
        Optional<Person> personOpt = Optional.ofNullable(person);
        personOpt.ifPresent(p -> System.out.println(p.getAge()));


        personOpt = Optional.ofNullable(null);
        Person person2 = personOpt.orElseGet(() -> getNewPerson());

        // orElseGet  是 Supplier<? extends T> other

        System.out.println(person2.getName());
    }

    private Person getNewPerson() {
        return new Person("测试other2", 19);
    }


    private void test3() {
        Person person = new Person("测试", 28);
        Parterner parterner = new Parterner(person);

        Optional<Parterner> parternerOpt = Optional.ofNullable(parterner);
        System.out.println(parternerOpt.map(pa -> pa.getPerson()).map(pe -> pe.getAge()).orElse(18));
        person.setAge(null);
        System.out.println(parternerOpt.map(pa -> pa.getPerson()).map(pe -> pe.getAge()).orElse(18));

        parterner.setPerson(null);
        System.out.println(parternerOpt.map(pa -> pa.getPerson()).map(pe -> pe.getAge()).orElse(18));

    }

    private void test4() {
        User user = new User();

        Address address = new Address();

        address.setCountry(new Country("USA"));

        user.setAddress(address);

        Optional<User> userOpt = Optional.ofNullable(user);

        System.out.println(userOpt.flatMap(u -> u.getAddress()).flatMap(a -> a.getCountry()).map(c -> c.getName()).orElse("china"));

        address.setCountry(null);

        System.out.println(userOpt.flatMap(u -> u.getAddress()).flatMap(a -> a.getCountry()).map(c -> c.getName()).orElse("china"));

        user.setAddress(null);
        System.out.println(userOpt.flatMap(u -> u.getAddress()).flatMap(a -> a.getCountry()).map(c -> c.getName()).orElse("china"));

    }

    private void test5() throws Exception {
        Person person = new Person("测试1", 28);

        if (person == null || isEmpty(person.getName())) {
            throw new Exception();
        }
        String name = person.getName();
        // 业务省略...

        // 使用Optional改造
        Optional.ofNullable(person).filter(s -> !isEmpty(s.getName())).orElseThrow(() -> new Exception());

        name = person.getName();
        // 业务省略...
        System.out.println(name);
    }

    private void test6() throws Exception {
        Person person = new Person("kane",18);
        Result<Person> result = Result.succeed(person);

//        result = Result.failed("没有数据");
//        System.out.println(getPersonName(result));
        System.out.println(getPersonNameNew(result));
    }

    public static String getPersonName(Result<Person> result) throws IllegalArgumentException {
        if (result != null) {
            Person p = result.getData();
            if (p != null) {
                return p.getName();
            }
        }
        throw new IllegalArgumentException("The value of param comp isn't available.");
    }

    public static String getPersonNameNew(Result<Person> result) throws IllegalArgumentException {
        return Optional.ofNullable(result)//
                .map(Result::getData)  // 相当于c -> c.getResult()，下同
                .map(Person::getName)
                .orElseThrow(()->new IllegalArgumentException("The value of param comp isn't available."));
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }


    public class Parterner {
        private Person person;

        public Parterner(Person person) {
            this.person = person;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }
    }

    public class Person {
        private String name;
        private Integer age;

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }


    public class User {
        private Address address;

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

    }

    public class Address {
        private Country country;

        public Optional<Country> getCountry() {
            return Optional.ofNullable(country);
        }

        public void setCountry(Country country) {
            this.country = country;
        }
    }

    public class Country {
        private String name;

        public Country(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public Optional deal(Person person) {
        return Optional.ofNullable(person);
    }
}
