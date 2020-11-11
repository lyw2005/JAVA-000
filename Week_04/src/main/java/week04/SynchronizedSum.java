package week04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedSum {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        SynchronizedSum run = new SynchronizedSum();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            System.out.println("My name is : " + Thread.currentThread().getName());
            run.sum(36);
        });
        int result = run.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        executor.shutdown();
    }

    private Integer value = null;

    public synchronized  void sum(int num) {
        value = fibo(num);
        this.notifyAll();
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public synchronized int getValue() throws InterruptedException {
        this.wait();
        return value;
    }
}
