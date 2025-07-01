package com.Collage.Controller;

import com.Collage.DTO.HobbiesDTO;
import com.Collage.DTO.ResponseDTO;
import com.Collage.Service.HobbiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/hobbies")
public class HobbiesController {

    @Autowired
    HobbiesService hobbiesService;

    @PostMapping("/save")
    public Mono<ResponseEntity<ResponseDTO>> saveHobbies(@RequestBody HobbiesDTO hobbiesDTO){
        return hobbiesService.create(hobbiesDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<ResponseDTO>> deleteHobbies(@PathVariable String id){
        return hobbiesService.deleteHobbies(id);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(@PathVariable String id) {
        return hobbiesService.getStudentById(id);
    }
}
