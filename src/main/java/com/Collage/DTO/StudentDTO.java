package com.Collage.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.List;

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

        @NotBlank(message = "Age is required")
        private int age;

        @NotBlank(message = "Department is required")
        private String department;

        @NotNull(message = "Phone number is required")
        private BigInteger phone;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String password;

        @NotNull(message = "feePaid number is required")
        private double feesPaid;

        @NotNull(message = "Address ID is required")
        private List<String> addressId;

       private List<String> hobbiesId;
}
