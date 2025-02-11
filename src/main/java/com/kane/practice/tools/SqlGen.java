package com.kane.practice.tools;

import java.io.FileWriter;
import java.io.IOException;

public class SqlGen {

    public static void main(String[] args) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `hbl_base`.`b_fee_period_policy_config` (")
                .append("`fee_policy_id`, `fee_code`, `fee_config_id`, `post_handle_type`, `post_handle`, `post_handle_rate_type`, `post_handle_rate`, `valid_time_type`, `start_valid_time`, `end_invalid_time`, `period`, `start_period`, `end_period`, `priority`, `status`, `remark`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES \n");

        int[] policyIds = {41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77};
        int[] configIds = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] fees = {70.0, 80.0, 100.0, 120.0, 150.0, 160.0, 240.0, 400.0, 80.0, 100.0, 120.0, 150.0, 200.0, 240.0, 300.0, 100.0, 120.0, 150.0, 200.0, 240.0, 300.0, 400.0, 120.0, 150.0, 200.0, 240.0, 300.0, 400.0, 100.0, 120.0, 150.0, 240.0, 300.0, 150.0, 200.0, 240.0, 300.0};

        for (int i = 0; i < policyIds.length; i++) {
            String feeCode = (policyIds[i] >= 69) ? "FEE_PLATFORM" : "FEE_SIM";
            sql.append("(")
                    .append(policyIds[i]).append(", '")
                    .append(feeCode).append("', ")
                    .append(configIds[i]).append(", 1, 1, 1, ")
                    .append(fees[i]).append(", 1, '2023-12-07 00:00:00', '2099-12-07 00:00:00', 1, 0, 0, 1, 1, NULL, '2023-12-07 00:00:00', 'yxz', '2023-12-07 00:00:00', 'yxz'")
                            .append(i < policyIds.length - 1 ? "),\n" : ");\n");
        }

        try (FileWriter writer = new FileWriter("generated_sql.sql")) {
            writer.write(sql.toString());
            System.out.println("SQL file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



