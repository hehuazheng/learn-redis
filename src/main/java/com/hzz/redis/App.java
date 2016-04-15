package com.hzz.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class App {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Jedis jedis = new Jedis("zkserver1", 6379);
		long end = System.currentTimeMillis();
		System.out.println("time consumed: " + (end - start));
		jedis.mset("name1", "yangw", "name2", "demon", "name3", "elena");
		System.out.println(jedis.mget("name1", "name2", "name3"));

		Map<String, String> user = new HashMap<String, String>();
		user.put("name", "cd");
		user.put("password", "123456");
		// map存入redis
		jedis.hmset("user", user);
		// mapkey个数
		System.out.println(String.format("len:%d", jedis.hlen("user")));
		// map中的所有键值
		System.out.println(String.format("keys: %s", jedis.hkeys("user")));
		// map中的所有value
		System.out.println(String.format("values: %s", jedis.hvals("user")));
		// 取出map中的name字段值
		List<String> rsmap = jedis.hmget("user", "name", "password");
		System.out.println(rsmap);
		// 删除map中的某一个键值 password
		jedis.hdel("user", "password");
		System.out.println(jedis.hmget("user", "name", "password"));

		// list
		jedis.del("listDemo");
		System.out.println(jedis.lrange("listDemo", 0, -1));
		jedis.lpush("listDemo", "A");
		jedis.lpush("listDemo", "B");
		jedis.lpush("listDemo", "C");
		System.out.println(jedis.lrange("listDemo", 0, -1));
		System.out.println(jedis.lrange("listDemo", 0, 1));

		// set
		jedis.sadd("sname", "wobby");
		jedis.sadd("sname", "kings");
		jedis.sadd("sname", "demon");
		System.out.println(String.format("set num: %d", jedis.scard("sname")));
		System.out.println(String.format("all members: %s",
				jedis.smembers("sname")));
		System.out.println(String.format("is member: %B",
				jedis.sismember("sname", "wobby")));
		System.out.println(String.format("rand member: %s",
				jedis.srandmember("sname")));
		// 删除一个对象
		jedis.srem("sname", "demon");
		System.out.println(String.format("all members: %s",
				jedis.smembers("sname")));
	}
}
