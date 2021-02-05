package com.automart.order.controller;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.cart.domain.Cart;
import com.automart.cart.repository.CartRepository;
import com.automart.order.dto.ResponseDataDto;
import com.automart.utility.HttpService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderControllerTest {

    @Autowired
    private HttpService httpService;

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void getUniqueMerchantUid() throws Exception {

        // given
        String email = "hello@naver.com";
        String merchantUid = "merchant_2021.02.04";

        // when
        int idx = merchantUid.indexOf("_"); // merchant_yyyy.mm.dd

        String pre = merchantUid.substring(0, idx); // marchant_
        String mid = Base64.getEncoder().encodeToString(email.getBytes());
        String end = merchantUid.substring(idx + 1); // yyyy.mm.dd

        // then
        String merchant_uid = pre + mid + end;
        System.out.println(merchant_uid);
    }

    @Test
    public void confirmV1() throws Exception {
        // given : 토큰을 요청한다.
        RestTemplate postTemplate = new RestTemplate();
        postTemplate.getMessageConverters().add(new StringHttpMessageConverter());


        String url1 = "https://api.iamport.kr/users/getToken";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.add("imp_key", "내용삭제");
        params.add("imp_secret", "내용삭제");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, header);

        // 이 경우 field 값으로 조회가능, 파싱시 tokenize가 필요
        // {name=홍길동, age=16, excercise=baseball}
        ResponseDataDto postResult = postTemplate.postForObject(url1, request, ResponseDataDto.class);

        System.out.println("post response : " + postResult.getResponse());

        String token = null;
        for (Field field : postResult.getResponse().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = field.get(postResult.getResponse()).toString();
            if (value.startsWith("access_token")) {
                int idx = value.indexOf("=");
                token = value.substring(idx + 1);
                break;
            }
        }
        System.out.println(token);


        // when : 토큰을 가지고 정보를 조회한다.
        RestTemplate getTemplate = new RestTemplate(); // 아임포트 서버에 REST API 요청
        getTemplate.getMessageConverters().add(new StringHttpMessageConverter());


        String url = "https://api.iamport.kr/payments/find/" + "내용삭제" + "/paid";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add("Authorization", token);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .build(false);

        try {
            // ResponseDataDto getResult = restTemplate.getForObject(url, ResponseDataDto.class); 이 경우는 header추가가 불가능이여서 안씀

            /* 이 경우엔 getDeclaredFields로 필드값 조회가 불가능. 아래처럼 나옴
             * field: private static final long java.util.LinkedHashMap.serialVersionUID
             * field: transient java.util.LinkedHashMap$Entry java.util.LinkedHashMap.head
             */
            ResponseDataDto<Map<String, Object>> getResult = getTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseDataDto<Map<String, Object>>>() {
                    }).getBody();

            Map<String, Object> map = getResult.getResponse();

            JSONObject json = new JSONObject();


            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                json.put(key, value);
            }
            System.out.println(json);
        } catch (Exception e) {
            System.out.println("Exception!! " + e.toString());
        }
        // then : 결과를 클라이언트에게 전달한다.
        System.out.println("amount와 paid를 return한다.");

    }


    // Utility로 정리 & MAP으로 반환. 응답이 매우느림
    @Test
    public void confirmV2() throws Exception {
        // given : 토큰을 요청한다.

        String postUrl = "https://api.iamport.kr/users/getToken";

        UriComponents postBuilder = UriComponentsBuilder.fromHttpUrl(postUrl)
                .build(false);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        // key 설정 삭제됨
        params.add("imp_key", "내용삭제");
        params.add("imp_secret", "내용삭제");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, header);

        ResponseDataDto postResult = httpService.post(postBuilder, request, ResponseDataDto.class);


        System.out.println("post response : " + postResult.getResponse());

        // 메소드 추출 불가능(cause:인스턴스만 조회가능)
        String token = null;
        for (Field field : postResult.getResponse().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = field.get(postResult.getResponse()).toString();
            if (value.startsWith("access_token")) {
                int idx = value.indexOf("=");
                token = value.substring(idx + 1);
                break;
            }
        }
        System.out.println(token);

        // encode 하면 제대로 요청이 되지 않는다. (build에 encode설정)
        String getUrl = "https://api.iamport.kr/payments/find/{merchant_uid}/paid";
        UriComponents getBuilder = UriComponentsBuilder.fromHttpUrl(getUrl)
                .build(false)
                .expand("내용삭제");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add("X-ImpTokenHeader", token);

        ResponseDataDto<Map<String, Object>> getResult = httpService.get(getBuilder, headers).getBody();
        System.out.println("ResponseEntity.body(getResult)를 리턴한다.");

    }

    // Utility로 정리 & JSON으로 반환
    @Test
    public void confirmV3() throws Exception {
        // given : 토큰을 요청한다.

        String postUrl = "https://api.iamport.kr/users/getToken";

        UriComponents postBuilder = UriComponentsBuilder.fromHttpUrl(postUrl)
                .build(false);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        // key 설정 삭제됨
        params.add("imp_key", "7812238860494184");
        params.add("imp_secret", "AJwbLyOum0KAkyuZBUX55Nx9cR3tof5YZC9KaYewZY7jxBi8wgdC3LMfCPI05NZ4q9PA73772frAQKIn");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, header);

        ResponseDataDto postResult = httpService.post(postBuilder, request, ResponseDataDto.class);


        System.out.println("post response : " + postResult.getResponse());

        // 메소드 추출 불가능(cause:인스턴스만 조회가능)
        String token = null;
        for (Field field : postResult.getResponse().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = field.get(postResult.getResponse()).toString();
            if (value.startsWith("access_token")) {
                int idx = value.indexOf("=");
                token = value.substring(idx + 1);
                break;
            }
        }
        System.out.println(token);

        // encode 하면 제대로 요청이 되지 않는다. (build에 encode설정)
        String getUrl = "https://api.iamport.kr/payments/find/{merchant_uid}/paid";
        UriComponents getBuilder = UriComponentsBuilder.fromHttpUrl(getUrl)
                .build(false)
                .expand("merchantdGVzdEBuYXZlci5jb20=161252265878");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add("X-ImpTokenHeader", token);

        ResponseDataDto<Map<String, Object>> getResult = httpService.get(getBuilder, headers).getBody();

        net.minidev.json.JSONObject jsonObject = httpService.MapConverterToJson(getResult.getResponse());

        String status = (String) jsonObject.get("status");

        if (status.equals("paid")) {
            Integer amount = (Integer) jsonObject.get("amount");

            Cart cart = cartRepository.findByNo(2).
                    orElseThrow(() -> new NotFoundDataException("카트를 불러올 수 없습니다."));

            System.out.println("cart 금액 : " + cart.getTotalPrice());
            System.out.println("amount 금액 : " + amount);

            if (amount != cart.getTotalPrice()) {
                System.out.println("true를 리턴한다.");
            }
        }
        System.out.println("false를 리턴한다.");
    }
}