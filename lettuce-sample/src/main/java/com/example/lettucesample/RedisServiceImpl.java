package com.example.lettucesample;

import io.lettuce.core.XAddArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {

	private final RedisConnection redisConnection;
	private final RedisConfiguration redisConfiguration;

	@Override
	public String addToStream(String streamName, String transactionId) {
		var currentSecond = Instant.now().getEpochSecond();
		var streamNameSecond = streamName + "-" + currentSecond;
		log.info("adding txn id {} to stream {}", transactionId, streamNameSecond);
		var commands = redisConnection.getCommands();
		commands.xadd(streamNameSecond, XAddArgs.Builder.maxlen(redisConfiguration.getMaxStreamLength()).approximateTrimming(), transactionId, "");
		commands.expire(streamNameSecond, redisConfiguration.getStreamExpireSeconds());
		return streamNameSecond;
	}

	@Override
	public Long getStreamLength(String streamKey) {
		return redisConnection.getCommands().xlen(streamKey);
	}
}