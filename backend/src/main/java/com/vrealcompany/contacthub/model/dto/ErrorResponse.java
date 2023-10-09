package com.vrealcompany.contacthub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
}
