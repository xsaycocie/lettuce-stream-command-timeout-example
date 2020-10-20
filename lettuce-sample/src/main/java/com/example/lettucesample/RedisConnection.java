package com.example.lettucesample;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class RedisConnection {

	private final GenericObjectPool<StatefulRedisConnection<String, String>> pool;

	public RedisConnection(RedisConfiguration configuration) {
		pool = createPool(configuration);
	}

	public RedisCommands<String, String> getCommands() {
		try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
			return connection.sync();
		} catch (Exception e) {
			throw new RedisException("[redisFailure] error attempting to connect to redis");
		}
	}

	private GenericObjectPool<StatefulRedisConnection<String, String>> createPool(RedisConfiguration redisConfiguration) {
		RedisURI redisUri = RedisURI.Builder.redis(redisConfiguration.getHost())
				.withSsl(redisConfiguration.getUseSsl())
				.withPort(redisConfiguration.getPort())
				.build();

		RedisClient client = RedisClient.create(redisUri);
		client.setOptions(ClientOptions.builder().autoReconnect(true).build());
		client.setDefaultTimeout(Duration.ofSeconds(redisConfiguration.getTimeout()));
		client.setOptions(ClientOptions.builder().build());

		return ConnectionPoolSupport.createGenericObjectPool(client::connect, createPoolConfig(redisConfiguration));
	}

	private <T> GenericObjectPoolConfig<T> createPoolConfig(RedisConfiguration redisConfiguration){
		GenericObjectPoolConfig<T> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxTotal(redisConfiguration.getPoolMaxTotal());
		poolConfig.setMaxIdle(redisConfiguration.getPoolMaxIdle());
		poolConfig.setBlockWhenExhausted(redisConfiguration.getPoolBlockWhenExhausted());
		poolConfig.setMaxWaitMillis(redisConfiguration.getPoolMaxWaitMillis());
		poolConfig.setMinIdle(redisConfiguration.getPoolMinIdle());
		return poolConfig;
	}
}