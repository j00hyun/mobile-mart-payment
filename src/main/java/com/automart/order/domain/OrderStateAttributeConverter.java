package com.automart.order.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStateAttributeConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String s) {
        if ("ORDER".equals(s)) {
            return 1;
        } else if ("CANCEL".equals(s)) {
            return 2;
        }
        return 1;
    }

    @Override
    public String convertToEntityAttribute(Integer code) {
        if(1 == code) {
            return "ORDER";
        } else if (2 == code) {
            return "CANCEL";
        }
        return "ORDER";
    }
}
