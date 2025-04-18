package com.kane.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test18 {
    public static void main(String[] args) throws IOException {
        // 读取桌面 11.txt 文件

        List<String> baseList = FileUtils.readLines(new File("/Users/kanepan/Desktop/11.txt"),"utf-8");

        String filePath = "data.txt"; // 替换为实际文件路径
        List<String> sqlList = new ArrayList<>();

        for (String line : baseList) {
            String[] parts = line.trim().split("\t");
            String machineCode = parts[0].trim();
            String companyId = parts[1].trim();

            // 生成标准 SQL 语句
            String sql = String.format(
                    "UPDATE zw_ht_machine_info " +
                            "SET node_code = 'paas', company_id = %s " +
                            "WHERE machine_code = '%s';",
                    companyId, machineCode
            );
            sqlList.add(sql);
        }

        // 输出结果
        sqlList.forEach(System.out::println);

        // 将结果写入文件
        File file = new File("/Users/kanepan/Desktop/22.txt");
        FileUtils.writeLines(file, sqlList);
        System.out.println("SQL 语句已写入文件: " + file.getAbsolutePath());

    }
}