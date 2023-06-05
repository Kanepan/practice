package com.kane.practice.concurrent.snowflake;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ShortBizUidGenerator implements BizUidGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUidGenerator.class);

    /**
     * Bits allocate
     */
    protected int timeBits = 30;  //28 支持 8年 29支持16年左右  30 32年左右 支持 32 支持 100年左右
    protected int workerBits = 5;   // 5位，支持31个workid
    protected int seqBits = 13; // 13 支持1秒生成8000左右(1024*8-1)  10支持1秒1023个 (1024 -1)

    /**
     * Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)
     */
    protected String epochStr = "2020-05-12";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);

    /**
     * Stable fields after spring bean initializing
     */
    protected BitsAllocator bitsAllocator;
    protected long workerId;

    protected volatile long lastSecond = -1L;
    protected static final ConcurrentMap<String, AtomicLong> SEQUENCE_MAP = new ConcurrentHashMap<>();


    /**
     * Spring property
     */
    protected WorkerIdAssigner workerIdAssigner;

    public void init() throws Exception {
        // initialize bits allocator
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);

        this.setEpochStr(epochStr);
        // initialize worker id
        if (workerIdAssigner != null) {
            workerId = workerIdAssigner.assignWorkerId();
            if (workerId > bitsAllocator.getMaxWorkerId()) {
                throw new RuntimeException("Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
            }
        }

        LOGGER.info("Initialized bits(1, {}, {}, {}) for workerID:{}", timeBits, workerBits, seqBits, workerId);
    }

    @Override
    public long getUID(String bizType) throws UidGenerateException {
        try {
            return nextId(bizType);
        } catch (Exception e) {
            LOGGER.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }


    @Override
    public String parseUID(long uid) {
        long longBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID 这里
//        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long sequence = uid << (longBits - sequenceBits) >>> (longBits - sequenceBits);
//        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long workerId = (uid << (longBits - sequenceBits - workerIdBits)) >>> (longBits - workerIdBits);
        long deltaSeconds = uid >>> (workerIdBits + sequenceBits);

        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
        String thatTimeStr = DateUtils.formatByDateTimePattern(thatTime);

        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, sequence);
    }

    /**
     * Get UID
     *
     * @return UID
     * @throws UidGenerateException in the case: Clock moved backwards; Exceeds the max timestamp
     */
    protected long nextId(String bizType) {
        long currentSecond = getCurrentSecond();

        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }

        // At the same second, increase sequence
        AtomicLong sequence = null;
        sequence = SEQUENCE_MAP.get(bizType);
        if (sequence == null) {
            sequence = new AtomicLong();
            AtomicLong tempLong = SEQUENCE_MAP.putIfAbsent(bizType, sequence);
            if (tempLong != null) {
                sequence = tempLong;
            }
        }
        synchronized (sequence) {
            if (currentSecond == lastSecond) {
                sequence.set(sequence.incrementAndGet() & bitsAllocator.getMaxSequence());
                // Exceed the max sequence, we wait the next second to generate uid
                if (sequence.get() == 0) {
                    currentSecond = getNextSecond(lastSecond);
                }
                // At the different second, sequence restart from zero
            } else {
                // reset
                sequence.set(0);
            }

            setLastSecond(currentSecond);
            // Allocate bits for UID
            return bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence.get());
        }

    }

    public  void setLastSecond(long currentSecond){
        this.lastSecond = currentSecond;
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }

    /**
     * Setters for spring property
     */
    public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
    }

    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }

    public static void main(String[] args) throws Exception {
        ShortBizUidGenerator shortBizUidGenerator = new ShortBizUidGenerator();
        shortBizUidGenerator.init();
        shortBizUidGenerator.setWorkerBits(7);

        shortBizUidGenerator.nextId("test");
        shortBizUidGenerator.nextId("test");
        shortBizUidGenerator.nextId("test");
        shortBizUidGenerator.nextId("test");
        long id = shortBizUidGenerator.nextId("test");
        System.out.println(id);
        System.out.println(shortBizUidGenerator.parseUID(id));
    }
}
