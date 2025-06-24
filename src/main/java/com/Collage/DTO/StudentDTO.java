package com.Collage.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentDTO {

        private String id;

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Department is required")
        private String department;

        @NotNull(message = "Phone number is required")
        private BigInteger phone;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotNull(message = "Address ID is required")
        private String addressId;
}
