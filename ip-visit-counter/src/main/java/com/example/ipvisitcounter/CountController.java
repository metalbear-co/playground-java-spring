package com.example.ipvisitcounter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountController {
    private final HttpServletRequest request;
    private final Settings settings;
    private final RedisService redis;
    private final KafkaProducerService kafka;
    private final IpInfoApi ipInfoApi;

    @Autowired
    public CountController(HttpServletRequest request, Settings settings, RedisService redis, KafkaProducerService kafka, IpInfoApi ipInfoApi) {
        this.request = request;
        this.settings = settings;
        this.redis = redis;
        this.kafka = kafka;
        this.ipInfoApi = ipInfoApi;
    }

    @GetMapping("/count")
    public CountResponse count() {
        String ip = request.getRemoteAddr();
        String tenant = request.getHeader("x-pg-tenant");

        KafkaMessage kafkaMessage = new KafkaMessage(ip);
        kafka.sendJsonMessage(kafkaMessage);

        Long count = redis.incrementKey(ip);
        IpInfo info = ipInfoApi.getIpInfo(ip, tenant);
        return new CountResponse(count, settings.getResponseFileData(), info);
    }
}
