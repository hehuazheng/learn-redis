package com.hzz.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

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

	public static String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			System.out.println(jedis.toString());
			return jedis.get(key);
		} catch (JedisConnectionException e) {
			if (jedis != null) {
				INSTANCE.returnBrokenResource(jedis);
				jedis = null;
			}
			throw e;
		} finally {
			if (jedis != null) {
				INSTANCE.returnResource(jedis);
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			final int idx = i;
			new Thread() {
				public void run() {
					System.out.println(idx + ": " + get("foo"));
				};
			}.start();
		}
	}

}
