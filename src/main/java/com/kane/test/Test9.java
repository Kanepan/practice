package com.kane.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test9 {
    static final Long a = 100L;

    public static void main(String[] args) throws InterruptedException {
        String a = "4352,5633,5635,6917,7686,6152,6153,7434,5642,6154,7435,6155,7436,6156,7437,7438,7439,7440,6416,6419,6423,5658,6427,7458,7459,5927,6953,6443,6955,5679,6704,6705,6706,6707,6708,6709,6710,7990,6711,7991,6712,6713,6714,6715,6203,6716,6718,7743,7744,7745,6722,7746,7747,6724,7748,7749,6727,6728,6729,6730,6731,6732,6733,6734,6735,6737,8017,6487,6241,6242,6243,5498,7546,7547,7309,7568,7825,7327,4768,7329,7330,7332,7333,5797,5801,7338,7850,7340,7597,7344,7345,7347,4275,4277,7093,4278,7094,4279,4280,4281,4282,4283,4284,7359,7360,7361,7362,7364,7365,7877,7878,7367,7880,7368,7117,7373,7376,5840,7377,7637,7639,7640,7641,7642,9434,7132,8159,7651,5604,4325,7397,7653,4326,7654,4327,7399,7655,4328,7656,4329,7657,4330,4331,4332,8172,4333,4334,4335,4336,4337,4338,4339,4340,4341,4342,4343,4344,4345,4346,4347,4348,4349,4350,4351";
        a = "4352,5633,5635,10756,6917,7686,6152,6153,7434,5642,6154,7435,6155,7436,6156,7437,9997,7438,7439,7440,6416,6419,6423,5658,6427,7458,10530,7459,11046,5927,6953,6443,6955,5679,6704,6705,6706,6707,6708,6709,6710,7990,6711,7991,6712,6713,6714,6715,6203,6716,6718,7743,7744,7745,6722,7746,7747,6724,7748,7749,6727,6728,6729,6730,6731,11084,6732,6733,6734,6735,6737,8017,6487,10078,6241,6242,6243,5498,7546,7547,10893,7309,7568,7825,11155,11156,7327,4768,7329,7330,7332,7333,5797,5801,7338,7850,7340,7597,7344,7345,7347,4275,4277,7093,4278,7094,4279,4280,4281,4282,4283,4284,10686,7359,7360,7361,7362,7364,7365,7877,7878,7367,7880,7368,7117,7373,7376,5840,7377,10452,7637,7639,7640,7641,7642,9434,7132,8159,7651,5604,4325,7397,7653,4326,7654,4327,7399,7655,4328,7656,4329,7657,4330,4331,4332,8172,4333,4334,4335,4336,4337,4338,4339,4340,4341,4342,10486,4343,4344,4345,4346,4347,4348,4349,4350,4351";
        System.out.println(a.length());



        System.out.println(Arrays.stream(a.split(",")).filter(s -> "11046".equals(s)).count());
        System.out.println(Arrays.stream(a.split(",")).filter(s -> "5801".equals(s)).count());

        System.out.println(Arrays.stream(a.split(",")).filter(s -> "4326".equals(s)).count());

        Map<String,String> map = new HashMap<>();
        map.put("1",null);
        System.out.println("" + map.get("1"));

    }
}
