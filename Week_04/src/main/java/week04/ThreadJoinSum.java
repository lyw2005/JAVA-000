package week04;

public class ThreadJoinSum {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        ThreadJoinSum run = new ThreadJoinSum();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        Thread t1 = new Thread(() -> {
            run.sum(36);
        });
        t1.start();

        t1.join();
        int result = run.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
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
