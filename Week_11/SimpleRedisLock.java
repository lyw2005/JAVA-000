package geek.hw.week11;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SimpleRedisLock {

    public SimpleRedisLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private JedisPool jedisPool;

    public void testConnection() {
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println(jedis.getClient().getPort());
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            System.out.println("服务正在运行：" + jedis.ping());
        }
    }

    public Boolean tryLock(String lockKey, String lockVal, int lockMaxLifeTime, int tryWaitingTime, int waitingSleepTime) {
        String luaScript = " if redis.call('set',KEYS[1],ARGV[1],'PX',ARGV[2],'NX') then return 1 else return 0 end ";
        Long tryBeginTime = System.currentTimeMillis();

        while (true) {
            Long result = null;
            try (Jedis jedis = jedisPool.getResource()) {
                result = (Long) jedis.eval(luaScript, Collections.singletonList(lockKey), Arrays.asList(lockVal, String.valueOf(lockMaxLifeTime)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Long.valueOf(1).equals(result)) {
                return true;
            }

            Long now = System.currentTimeMillis();
            if (now - tryBeginTime >= tryWaitingTime) {
                return false;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(waitingSleepTime);
            } catch (InterruptedException ignored) {
            }
        }

    }

    public void releaseLock(String lockKey, String lockVal) {
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end ";
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.eval(luaScript, Collections.singletonList(lockKey), Collections.singletonList(lockVal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimpleRedisLock redisLock = new SimpleRedisLock(new JedisPool(new GenericObjectPoolConfig(), "127.0.0.1", 6379, 2000));
        redisLock.testConnection();

        IntStream.rangeClosed(1, 20).forEach(i -> {
            new Thread(() -> {
                String lockKey = "LOCK";
                String threadName = Thread.currentThread().getName();

                Boolean locked = redisLock.tryLock(lockKey, threadName, 30000, 50000, 200);
                try {
                    if (locked) {
                        System.out.println("线程 : " + threadName + " 拿到锁了！搞什么东西..");
                        //处理东西很慢...
                        TimeUnit.SECONDS.sleep(2);
                    } else {
                        System.err.println("线程 : " + threadName + " 没拿到锁！");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    redisLock.releaseLock(lockKey, threadName);
                }
            }).start();
        });

    }
}
