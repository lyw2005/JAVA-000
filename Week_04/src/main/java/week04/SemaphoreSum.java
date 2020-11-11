package week04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreSum {

    Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        SemaphoreSum run = new SemaphoreSum();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        ExecutorService executor = Executors.newFixedThreadPool(1);
        run.semaphore.acquire();
        executor.execute(() -> {
            try {
                run.sum(36);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        run.semaphore.acquire();
        //这是得到的返回值
        int result = run.getValue();
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        run.semaphore.release();
        executor.shutdown();
    }

    private Integer value = null;


    public void sum(int num) throws InterruptedException {
        value = fibo(num);
        semaphore.release();
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue() {
        return this.value;
    }
}
