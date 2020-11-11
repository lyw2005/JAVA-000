学习笔记





作业一、思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？

思路如下：

1.同步运行，直接在主线程Sleep，等待线程运行完后再执行；

2.线程的Join，计算完成结果Join会main线程后执行；

3.Synchronized同步，配合wait()取值时等待计算sum的notifyAll()信号，表示计算完成；

4.Lock/Condition方案，与3类似；

5.Future方式，线程池submit并返回一个Future<Integer>，main线程中get()阻塞等待计算结果；

6.FutureTask方式，与Future方式类似，返回FutureTask<Integer>，main线程中get()阻塞等待计算结果；

7.CompletableFuture方式，CompletableFuture.supplyAsync一个有Supplier<U>，同样main线程中get()阻塞等待计算结果；

8.CountDownLatch，设置CountDownLatch的state为1，线程中执行sum后，cdl直接countDown()减1，然后main线程中await()等待；

9.CyclicBarrier，类似CountDownLatch，声明CyclicBarrier时，设置为state为1，并在回调中执行main，sum计算完成后，调用barrier的await();告知完成可进行回调执行；并不重置Barrier，一遍运行完成后结束；

10.Semaphore，声明Semaphore时，只设置一个信号，在启动线程/提交到线程池之前acquire() 1个信号，在计算完成后release()掉一个信号，main线程中获取计算结果之前必须acquire()信号，sum没有计算完没有信号只能等待完成才能继续；



代码见包src\main\java\week04



