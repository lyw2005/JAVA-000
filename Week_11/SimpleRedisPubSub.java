package geek.hw.week11;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class SimpleRedisPubSub extends JedisPubSub {


    @Override
    public void onMessage(String channel, String message) {
        System.out.println("收到消息成功！ channel: " + channel + ", message: " + message);
        if (message.equals("close"))
            this.unsubscribe();
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("订阅频道成功！ channel:  " + channel + ", subscribedChannels: " + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("取消订阅频道！ channel: " + channel + ", subscribedChannels: " + subscribedChannels);

    }


    @Test
    public void redisSubTest() {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6379, 0);// redis服务地址和端口号
            JedisPubSub jedisPubSub = new SimpleRedisPubSub();
            jedis.subscribe(jedisPubSub, "order");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }

    @Test
    public void redisPubTest() {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6379, 0);// redis服务地址和端口号
            jedis.publish("order", "新到的订单信息！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }


}
