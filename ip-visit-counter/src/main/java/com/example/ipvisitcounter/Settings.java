package com.example.ipvisitcounter;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class Settings {

    @Value("${REDISADDRESS}")
    private String redisAddress;

    public String getRedisHost() {
        return redisAddress.split(":")[0];
    }

    public int getRedisPort() {
        return Integer.parseInt(redisAddress.split(":")[1]);
    }

    @Value("${RESPONSEFILE}")
    private String responseFilePath;

    private String responseFileData;

    public String getResponseFileData() {
        return responseFileData;
    }

    @PostConstruct
    public void loadResponseFile() throws IOException  {
        responseFileData = Files.readString(Path.of(responseFilePath));
    }

    @Value("${KAFKAADDRESS}")
    private String kafkaAddress;

    public String getKafkaAddress() {
        return kafkaAddress;
    }

    @Value("${KAFKATOPIC}")
    private String kafkaTopic;

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    @Value("${IPINFOADDRESS}")
    private String ipInfoAddress;

    public String getIpInfoAddress() {
        return ipInfoAddress;
    }
}
