package com.kane.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test66 {
    public static void main(String[] args) throws IOException {
        List<String> baseList = FileUtils.readLines(new File("/Users/kanepan/Desktop/33.txt"),"utf-8");


        List<String> sqlList = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        for (String line : baseList) {

            String machineCode = line.trim();

            sb.append("'" + machineCode + "',");
        }

        System.out.println(sb.toString());

    }
}
