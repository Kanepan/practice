package com.kane.practice.efficiency;

import com.kane.practice.utils.http.HttpClientUtil;
import com.kane.practice.utils.http.SupplyResult;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TpsTest {

    private static ExecutorService es = Executors.newFixedThreadPool(30);

    public static void deal(){
        int sendCount = 1000;
        Set<Callable<SupplyResult<String>>> set = new HashSet<>();
        //1000114400018278739
        final long card = 1000114400010217839l;
        for (int i = 0; i < sendCount; i++) {
            final int count = i;
            set.add(new Callable<SupplyResult<String>>() {
                @Override
                public SupplyResult<String> call() throws Exception {
                    long temp = card + count;
                    SupplyResult<String> result =  HttpClientUtil.get("http://app.leyaozhineng.com/api/getVersion.action" );
//                    SupplyResult<String> result =  HttpClientUtil.get("http://127.0.0.1:8888/gascard/query/" + temp + "/1");
                     System.out.println(result.getModule());
                   return result;
                }
            });
        }

        long time = System.currentTimeMillis();

        try {
            es.invokeAll(set);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long time2 = System.currentTimeMillis();
        long cost = (time2 - time)/1000;
        System.out.println("cost:" + cost );
        System.out.println("tps:" + sendCount/cost );
        System.out.println("avs:" +  (time2 - time) /sendCount   );
        es.shutdown();
    }

    public static void main(String[] args) {
      deal();

    }

}
