package week04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockSum {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        LockSum run = new LockSum();
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

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    private Integer value = null;

    public  void sum(int num) {
        try {
            lock.lock();
            value = fibo(num);
            condition.signalAll();
        }finally {
            lock.unlock();

        }
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue() throws InterruptedException {
        try {
            lock.lock();
            condition.await();
            return value;
        }finally {
            lock.unlock();
        }
    }
}
