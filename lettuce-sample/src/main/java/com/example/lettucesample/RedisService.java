package com.example.lettucesample;

public interface RedisService {
    String addToStream(String streamName, String transactionId);
    Long getStreamLength(String streamKey);
}