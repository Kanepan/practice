package com.kane.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Date;

public class CopilotTest {

    public static void main(String[] args) {
        //calculate days between two dates
        Date date1 = new Date();
        Date date2 = new Date();
        int days = calculateDaysBetweenTwoDates(date1, date2);
        System.out.println("dys between two dates: " + days);

        int[] nums  = new int[]{1, 63, 24, 56, 4};
        //冒泡排序
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }

        System.out.println("冒泡排序结果：" + Arrays.toString(nums));

        //快速排序
        quickSort(nums, 0, nums.length - 1);

        System.out.println("快速排序结果：" + Arrays.toString(nums));



    }

    //快速排序
    private static void quickSort(int[] nums, int i, int i1) {
        if (i >= i1) {
            return;
        }
        int left = i;
        int right = i1;
        int pivot = nums[left];
        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }
        nums[left] = pivot;
        quickSort(nums, i, left - 1);
        quickSort(nums, left + 1, i1);
    }

    static int calculateDaysBetweenTwoDates(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    //copy bean fields  from source to target
     public static void copyBeanFields(Object source, Object target) {
        try {
            BeanUtils.copyProperties(target, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

     //校验身份证号码
    public static boolean checkIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }
        String[] idCardArray = idCard.split("");
        int[] idCardInt = new int[18];
        for (int i = 0; i < idCardArray.length; i++) {
            idCardInt[i] = Integer.parseInt(idCardArray[i]);
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += idCardInt[i] * (Math.pow(2, 17 - i) % 11);
        }
        int mod = sum % 11;
        int[] checkCode = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
        return checkCode[mod] == idCardInt[17];
    }



}
