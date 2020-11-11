package week04;

import java.util.concurrent.*;

public class CyclicBarrierSum {

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        long start = System.currentTimeMillis();
        CyclicBarrierSum run = new CyclicBarrierSum();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CyclicBarrierSum.cyclicBarrier = new CyclicBarrier(1, () -> {
            int result = run.getValue(); //这是得到的返回值

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + result);

            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

            // 然后退出main线程
            executor.shutdown();
        });
        executor.execute(() -> {
            System.out.println("My name is : " + Thread.currentThread().getName());
            try {
                run.sum(36);
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private Integer value = null;
    public static CyclicBarrier cyclicBarrier;
    public void sum(int num) throws BrokenBarrierException, InterruptedException {
        value = fibo(num);
        cyclicBarrier.await();
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
