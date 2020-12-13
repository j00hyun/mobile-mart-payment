package com.automart.user.Service;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@AllArgsConstructor
@Service
@Slf4j
public class SMSService {

    /**
     * 인증번호 전송
     * @param phone : 핸드폰 번호
     * @param message : 메세지 내용
     */
    @Transactional
    public void sendMessage(String phone, String message) {

        String api_key = "NCSDTOVXDJYJ4ZJV";
        String api_secret = "AXJSK5XLJMN7L7R9KENJD3KGKMZXIPCQ";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phone);
        params.put("from", "01041026206");
        params.put("type", "SMS");
        params.put("text", message);
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            log.info("message : " + message);
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

        } catch (CoolsmsException e) {

            log.error("메세지 전송 실패");
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}

