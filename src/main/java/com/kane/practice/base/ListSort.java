package com.kane.practice.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ListSort {

    public static void main(String[] args) {
        List<Account> list = new ArrayList<>();

        Account account1 = new Account();
        account1.setBalance(300L);
        list.add(account1);
        Account account2 = new Account();
        account2.setBalance(500L);
        list.add(account2);

        list.forEach(account -> System.out.println(account.getBalance()));
        System.out.println("====================================");
        //sort by balance
        list.sort((o1, o2) -> o2.getBalance().compareTo(o1.getBalance()));
        list.forEach(account -> System.out.println(account.getBalance()));

    }

    @Data
    static public class Account {
        private Long balance;
    }
}
