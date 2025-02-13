package com.kane.practice.tools;

public class SqlCreator2 {


    public static void main(String[] args) {

        String sql = "INSERT INTO `base_bus`.`c_company_sys_perms` (`perms_id`, `company_id`, `organization_id`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) SELECT 1718829213725597666, company_id, organization_id, 1, NULL, 'admin', NOW(), 'admin', NOW() FROM c_company_organization WHERE organization_id = 10000;";

        String merchantIds = "2605\n" +
                "13220\n" +
                "14104\n" +
                "5476\n" +
                "5696\n" +
                "5697\n" +
                "5698\n" +
                "5699\n" +
                "6139\n" +
                "6238\n" +
                "17793\n" +
                "18132\n" +
                "18133\n" +
                "18134";

        String[] merchantIdArray = merchantIds.split("\n");
        for (String merchantId : merchantIdArray) {
            System.out.println(sql.replace("10000", merchantId));
        }
    }
}
