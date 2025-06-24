package com.Collage.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String id;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 100, message = "Street name must be less than 100 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City name must be less than 50 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State name must be less than 50 characters")
    private String state;

    @NotBlank(message = "Zip code cannot be blank")
    @Size(min = 5, max = 10, message = "Zip code must be between 5 and 10 characters")
    private String zipCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country name must be less than 50 characters")
    private String country;
}

