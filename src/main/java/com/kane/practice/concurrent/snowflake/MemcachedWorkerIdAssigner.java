package com.kane.practice.concurrent.snowflake;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemcachedWorkerIdAssigner implements WorkerIdAssigner {
    private MemcachedClient c;

    {
        try {
            c = new MemcachedClient(
                    new InetSocketAddress("112.124.105.0", 11211));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MemcachedWorkerIdAssigner(int workerIdBits) {
        this.workerIdBits = workerIdBits;
    }

    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public void init() {

    }

    private final static String snowFlakeRegKey = "snowflakeReg_";

    private final static String snowFlakeRegIncrKey = "snowflakeRegIncr_";

    private int workerIdBits = 5;

    private final static int exp = 60 * 5;

    public void keepAlive(Long workId) {
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                OperationFuture<Boolean> touch = c.touch(snowFlakeRegKey + workId, exp);
                if (!touch.getStatus().isSuccess()) {
                    //防止版本太低，不支持touch
                    c.set(snowFlakeRegKey + workId, exp, "true");
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public Long getId() {
        Integer maxWorkId = getMaxWorkId();
        Boolean flag = null;
        long workId = 0;
        for (int i = 0; i < maxWorkId * 4; i++) {
            Long count = c.incr(snowFlakeRegIncrKey, 1, 1);
            if (count == null) {
                return null;
            }

            workId = count % maxWorkId;
            flag = c.add(snowFlakeRegKey + workId, exp, "true").getStatus().isSuccess();
            if (flag) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        if (!flag) {
            return null;
        }
        //
        keepAlive(workId);

        return workId;
    }

    public static void main(String[] args) {
        System.out.println(~(-1L << 5));
    }

    private Integer getMaxWorkId() {
//        StringBuffer sb = new StringBuffer("");
//        for (int i = 0; i < workerIdBits; i++) {
//            sb.append("1");
//        }
//        return Integer.valueOf(sb.toString(), 2);
        Long max = ~(-1L << workerIdBits);
        return max.intValue();
    }

    @Override
    public long assignWorkerId(String bizType) {
        //TODO
        return 0;
    }


    @Override
    public long assignWorkerId() {
        Long result = getId();
        if (result == null) {
            return 0;
        }
        return result;
    }
}
