package com.kane.practice.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kanepan
 */
public class ListRemoveIfTest {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        arr.removeIf(i -> i % 2 == 0);
        System.out.println(arr);
        //  UnsupportedOperationException
        arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        arr.removeIf(i -> i % 2 == 0);
        System.out.println(arr);

    }
}
