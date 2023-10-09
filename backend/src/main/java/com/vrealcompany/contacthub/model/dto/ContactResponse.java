package com.vrealcompany.contacthub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String website;
    private CompanyResponse company;
}
