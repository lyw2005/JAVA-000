package geek.hw.week11;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SimpleRedisCounter {

    private final String inventoryKey = "INVENTORY_KEY";

    private final JedisPool jedisPool;

    public SimpleRedisCounter(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void testConnection() {
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println(jedis.getClient().getPort());
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            System.out.println("服务正在运行：" + jedis.ping());
        }
    }

    public void initInventory() {
        try (Jedis jedis = jedisPool.getResource()) {
            Long defaultInventory = 10L;
            jedis.set(inventoryKey, String.valueOf(defaultInventory));
        }
    }

    /**
     * 按照步骤-1
     */
    public Long decrInventory() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(inventoryKey);
        }
    }

    public void incrInventory() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.incr(inventoryKey);
        }
    }

    public Long getInventory() {
        try (Jedis jedis = jedisPool.getResource()) {
            return Long.parseLong(jedis.get(inventoryKey));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleRedisCounter redisCounter = new SimpleRedisCounter(new JedisPool(new GenericObjectPoolConfig(), "127.0.0.1", 6379, 2000));
        redisCounter.testConnection();
        redisCounter.initInventory();
        System.out.println("线程 : " + Thread.currentThread().getName() + ": 还有库存 " + redisCounter.getInventory());
        System.out.println("************");
        IntStream.rangeClosed(1, 20).forEach(i -> new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            Long count = redisCounter.decrInventory();
            if(count >= 0){
                System.out.println("线程 : " + threadName + ": 执行减库存！");
            }else{
                redisCounter.incrInventory();
                System.out.println("线程 : " + threadName + ": 不执行减库存！");
            }
        }).start());

        TimeUnit.SECONDS.sleep(3);
        System.out.println("线程 : " + Thread.currentThread().getName() + ": 最终还有库存 " + redisCounter.getInventory());
        System.out.println("************");
    }

}
