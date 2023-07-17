package com.kane.practice.program.validate;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PasswordValidator {
    public static boolean isValidPassword(String password) {
        String pattern = "^(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*\\d){1,})(?=(.*[!@#$%^&*]){1,})[A-Za-z\\d!@#$%^&*]{8,}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String password = "abc123!@#";
        boolean isValid = isValidPassword(password);
        if (isValid) {
            System.out.println("密码有效");
        } else {
            System.out.println("密码无效");
        }
    }
}