package com.example.lettucesample;

import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/test/v1")
public class TestController {

    private final RedisService redisService;

    public TestController(RedisService redisService) {
        this.redisService = redisService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testRedisStream(HttpServletRequest request) {
        String txnId = request.getHeader("X-TxnId");
        log.info("transaction-id: {}", txnId);

        try {
            var streamOne = redisService.addToStream("streamOne", txnId);
            redisService.getStreamLength(streamOne);
            var streamTwo = redisService.addToStream("streamTwo", txnId);
            redisService.getStreamLength(streamTwo);
        } catch (RedisCommandTimeoutException e) {
            log.error("redisFailure");
            throw e;
        }
        return "";
    }
}
