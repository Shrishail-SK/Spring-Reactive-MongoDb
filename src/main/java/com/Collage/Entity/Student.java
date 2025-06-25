package com.Collage.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
@Document(collection = "student")
public class Student {
    @Id
    private String id;
    private String name;
    private String department;
    private BigInteger phone;
    private String email;
    private String password;
    private List<ObjectId> addressId;

}