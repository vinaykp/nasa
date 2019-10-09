package com.example.nasa.service;

import com.example.nasa.model.Mars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NasaApi {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${apiKey}")
    private String apiKey;
    @Value("${apiUrl}")
    private String apiUrl;


    public ResponseEntity<Mars> callMarsPhotosAPI(String earth_date) {
        String url = apiUrl + "&earth_date=" + earth_date + "&api_key=" + apiKey;
        log.info("Request for Mars Rover photos for : {}", earth_date);
        ResponseEntity<Mars> responseEntity = restTemplate.getForEntity(url, Mars.class);
        return responseEntity;
    }
}
