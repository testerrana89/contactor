package com.vrealcompany.contacthub.model.dto;

import lombok.Data;

@SuppressWarnings("unused")
@Data
public class LoginRequest {
    private String email;
    private String password;
}
