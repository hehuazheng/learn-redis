package com.hzz.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 使用JedisPool时，使用完成后需要释放，调用pool.destroy();<br />
 * 参照： http://www.tagspert.com/2011/08/tomcat-redis/ <br />
 * 在tomcat中时使用 ServletContextListener 来实现
 */
public class JedisPoolDemo {
	private static transient JedisPool INSTANCE = null;

	public static JedisPool getPool() {
		if (INSTANCE == null) {
			synchronized (JedisPoolDemo.class) {
				if (INSTANCE == null) {
					INSTANCE = new JedisPool("zkserver1", 6379);
				}
			}
		}
		return INSTANCE;
	}

	public static Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			System.out.println(jedis.toString());
			return jedis;
		} catch (JedisConnectionException e) {
			if (jedis != null) {
				INSTANCE.returnBrokenResource(jedis);
				jedis = null;
			}
			throw e;
		}
	}

	public static void main(String[] args) {
		Jedis jedis = getJedis();
		String key = "test-key-hzz";
		boolean exist = jedis.exists(key);
		System.out.println(exist);
		// 设置成功返回1
		long res = jedis.setnx(key, "hzz");
		System.out.println("res: " + res);
		// 设置成功返回0
		res = jedis.setnx(key, "hzz2");
		System.out.println("res: " + res);
		if (jedis != null) {
			INSTANCE.returnResource(jedis);
		}
	}

}
