package com.kane.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Test5 {
    public static void main(String[] args) {
        BigDecimal b1 = BigDecimal.valueOf(0.3);

        System.out.println(b1.compareTo(BigDecimal.valueOf(9.1)));

        List<Person> lists = select();

        // lists to List<String>
        List<String> json = JSONArray.parseArray(JSON.toJSONString(lists), String.class);

        System.out.println(json);
        System.out.println(json.size());

        System.out.println(json.get(0));

        List<Person> lists2 = JSONArray.parseArray(json.toString(), Person.class);

        System.out.println(lists2);
        System.out.println("---------");

        lists.stream().filter(item -> item != null ).skip(0).limit(1).forEach(item -> System.out.println(item));

        System.out.println("---------");

        lists.stream().filter(item -> item.getAge() == 18).forEach(item -> System.out.println(item));

        System.out.println("---------");
        lists.stream().forEach(item -> System.out.println(item));

    }

    private static List<Person> select(){
        Person person = new Person();
        person.setName("kane");
        person.setAge(18);

        Person person2 = new Person();
        person2.setName("tttt");
        person2.setAge(19);

        List<Person> lists = new ArrayList<>();
        lists.add(person);
        lists.add(person2);
        return lists;
    }
    @Data
   static class Person{
        private String name;
        private Integer age;
    }
}
