学习笔记

周四作业：

|       |                           SerialGC                           |                          ParallelGC                          |                             CMS                              |                              G1                              |
| :---: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| 256M  | ![image-20201028210518232](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210518232.png) | ![image-20201028210705724](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210705724.png) | ![image-20201028210834537](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210834537.png) | ![image-20201028211200489](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028211200489.png) |
| 512M  | ![image-20201028210454472](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210454472.png) | ![image-20201028210645104](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210645104.png) | ![image-20201028210859671](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210859671.png) | ![image-20201028211128515](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028211128515.png) |
| 1024M | ![image-20201028210407795](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210407795.png) | ![image-20201028210623269](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210623269.png) | ![image-20201028210924799](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210924799.png) | ![image-20201028211111383](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028211111383.png) |
| 2048M | ![image-20201028210332647](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210332647.png) | ![image-20201028210549283](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210549283.png) | ![image-20201028210951915](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028210951915.png) | ![image-20201028211047749](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201028211047749.png) |



SerialGC：串行收集器是最简单的，效率差，通过-XX:+UseSerialGC可以使用串行垃圾回收器。

ParallelGC：JDK8默认收集器，使用多线程来扫描和压缩堆，并行收集器同样有个缺点就是在它执行 minor或者 full 垃圾回收时将会停止所有的应用程序线程。并行收集器最适合应用程序，可以容忍应用程序的暂停，并试图优化来降低收集器导致的CPU开销。

CMS：带有Concurrent的是并发执行，并不影响业务时间，Concurrent操作的时候，默认占用系统线程数的1/4。主要影响业务时间的是两个阶段 Initial Mark（初始标记） 和 Final Remark（最终标记） 这两个阶段会产生SWT，即所有线程数都在干这一件事情，没有多余的线程处理业务

G1： G1收集器利用多个后台线程来扫描堆，将其划分为多个区域，范围从1MB到32MB（取决于堆的大小）。 G1收集器首先会去扫描那些包含最多垃圾对象的区域。这个收集器会出现STW的情况，就是在后台线程完成扫描未使用的对象之前堆被如果被耗尽的话，在这种情况下，收集器将不得不停止应用程序然后进入STW收集的状态。 G1还有另一个优点，就是它在执行的过程中可以顺便对堆进行压缩，这个能力CMS收集器只能在full STW收集期间才能做的。



总结：当堆内存比较小时，都会发生频繁的GC，堆内存比较大时，几个GC表现都比较好。所以不管使用哪类GC需要结合实际业务场景，设置合适的GC收集器，堆内存，线程数等参数。