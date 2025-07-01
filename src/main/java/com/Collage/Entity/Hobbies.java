package com.Collage.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Hobbies {

    @Id
    private String id;
    private String hobbyName;

}
