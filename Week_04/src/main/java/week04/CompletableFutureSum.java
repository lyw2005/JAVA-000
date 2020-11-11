package week04;

import java.util.concurrent.*;

public class CompletableFutureSum {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CompletableFutureSum run = new CompletableFutureSum();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("My name is : " + Thread.currentThread().getName());
            run.sum(36);
            return run.getValue();
        }, executor);
        int result = completableFuture.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        executor.shutdown();
    }

    private Integer value = null;

    public void sum(int num) {
        value = fibo(num);
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue() {
        return value;
    }
}
