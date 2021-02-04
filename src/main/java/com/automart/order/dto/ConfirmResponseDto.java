package com.automart.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ConfirmResponseDto implements Serializable{

    private int code;
    private String message;
    private Object response;

}
