package com.example.ipvisitcounter;

import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
public class IpInfoApi {
    private final String apiUrl;

    @Autowired
    public IpInfoApi(Settings settings) {
        this.apiUrl = settings.getIpInfoAddress();
    }
    public IpInfo getIpInfo(String ip, String tenant) {
        RestTemplate restTemplate = new RestTemplate();
        if (tenant != null) {
            restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
                HttpHeaders headers = request.getHeaders();

                headers.add("X-PG-Tenant", tenant); // Add your custom header here
                return execution.execute(request, body);
            }));
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("/ip/").path(ip);
        return restTemplate.getForObject(builder.build().toUri(), IpInfo.class);
    }
}