package com.kane.practice.concurrent.threadstop;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class ThreadFirstSuccess {

    public static void main(String[] args) {
        List<Callable<Integer>> taskList = new ArrayList<>();
        taskList.add(() -> {
            int waitTime = RandomUtils.nextInt(0, 2000);
            System.out.println("task1 waitTime=" + waitTime);
            Thread.sleep(waitTime);
            return waitTime;
        });
        ThreadFirstSuccess.supplyBatchPredicateTask(taskList, null, 1000, Executors.newFixedThreadPool(10));
    }

    private void predicate(){

    }

    public static <T> T supplyBatchPredicateTask(List<Callable<T>> taskList, Predicate<T> predicate, long timeout, Executor executor) {
        CountDownLatch predicateCountDown = new CountDownLatch(1);
        AtomicReference<T> successReference = new AtomicReference<>();
        AtomicReference<T> failReference = new AtomicReference<>();

        List<CompletableFuture<T>> futureList = new ArrayList<>(taskList.size());

        for (Callable<T> task : taskList) {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
                try {
                    T result = task.call();
                    if (predicate.test(result)) {
                        successReference.set(result);
                        predicateCountDown.countDown();
                        return result;
                    }
                    failReference.set(result);
                } catch (Exception exception) {

                    System.err.println("act=supplyBatchPredicateTask 任务执行失败" + exception.getMessage());
                    exception.printStackTrace();

                }
                return null;
            }, executor);
            futureList.add(future);
        }

//全失败(不仅仅是任务失败，还有是断言失败)的情況下进行唤醒
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).whenComplete((result, throwable) -> {
            predicateCountDown.countDown();
        });

        try {
            predicateCountDown.await(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
// ignoreø
            System.err.println("act=supplyBatchPredicateTask 等待任务执行结果中断" + e.getMessage());
            e.printStackTrace();

        }
        return successReference.get() != null ? successReference.get() : failReference.get();
    }
}
