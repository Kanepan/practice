package com.kane.practice.concurrent.threadstop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThreadStopTest {

    public static <T> CompletableFuture<T> firstSuccessful(
            List<CompletableFuture<T>> futures, Predicate<T> predicate) {
        if (futures == null || futures.isEmpty()) {
            throw new RuntimeException("No successful result found");
        }
        CompletableFuture<T> success = new CompletableFuture<>();
        AtomicInteger ref = new AtomicInteger(futures.size());
        Consumer<T> consumer =
                t -> {
                    try {
                        // 如果谓词通过且还没有其他成功的.
                        if (predicate.test(t) && !success.isDone()) {
                            success.complete(t);
                        }
                    } finally {
                        // 如果所有的都结束了, 还是没有结果. 那么返回异常.
                        if (ref.decrementAndGet() == 0) {
                            success.completeExceptionally(
                                    new RuntimeException("No successful result found"));
                        }
                    }
                };
        // 获取所有异步的结果
        for (CompletableFuture<T> future : futures) {
            future.whenCompleteAsync(
                    ((t, throwable) -> {
                        if (throwable != null) {
                            consumer.accept(null);
                        } else {
                            // 没有异常时. 去判断是否可以 success.
                            consumer.accept(t);
                        }
                    }));
        }
        return success;
    }
}
