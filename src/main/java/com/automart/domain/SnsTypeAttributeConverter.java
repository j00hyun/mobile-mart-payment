package com.automart.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SnsTypeAttributeConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String s) {
        if ("NAVER".equals(s)) {
            return 1;
        } else if ("KAKAO".equals(s)) {
            return 2;
        } else if ("GOOGLE".equals(s)) {
            return 3;
        }
        return 0;
    }

    @Override
    public String convertToEntityAttribute(Integer code) {
        if(1 == code) {
            return "NAVER";
        } else if (2 == code) {
            return "KAKAO";
        } else if (3 == code) {
            return "GOOGLE";
        }
        return "NULL";
    }
}
