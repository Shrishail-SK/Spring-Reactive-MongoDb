package com.Collage.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentWithAddressDTO {
    private String id;
    private String name;
    private String department;
    private BigInteger phone;
    private String email;
    private String password;
    private List<AddressDto>  addresses;
}
