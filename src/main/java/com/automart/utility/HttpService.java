package com.automart.utility;

import com.automart.order.dto.ResponseDataDto;
import net.minidev.json.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Service
@Component
public class HttpService
{
    private static final Logger logger = LogManager.getLogger(HttpService.class);
    private final RestTemplate restTemplate;

    @Autowired
    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter()); // JSON
    }


    public ResponseEntity get(String url)
    {
        return restTemplate.getForEntity(url, String.class);
    }


    public ResponseEntity get(String url, HttpHeaders headers){
        HttpEntity<String> entity= new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity  get(UriComponents builder)
    {
        return restTemplate.getForEntity(builder.toUriString(), String.class);
    }


    public ResponseEntity get(UriComponentsBuilder builder, HttpHeaders httpHeaders){

        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity, String.class);
    }

    /* 이 경우엔 getDeclaredFields로 필드값 조회가 불가능. 아래처럼 나옴
     * field: private static final long java.util.LinkedHashMap.serialVersionUID
     * field: transient java.util.LinkedHashMap$Entry java.util.LinkedHashMap.head
     */
    public ResponseEntity<ResponseDataDto<Map<String, Object>>> get(UriComponents builder, HttpHeaders httpHeaders) {
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                new ParameterizedTypeReference<ResponseDataDto<Map<String, Object>>>() {});
    }

    // 이 경우 field 값으로 조회가능, 파싱시 tokenize가 필요
    // {name=홍길동, age=16, excercise=baseball}
    public <T,P> T post(UriComponents builder, HttpEntity<P> entity, Class<T> response){
        return restTemplate.postForObject(builder.toUriString(), entity, response);
    }

    public JSONObject MapConverterToJson(Map<String,Object> map) {
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            json.put(key, value);
        }

        return json;
    }
}