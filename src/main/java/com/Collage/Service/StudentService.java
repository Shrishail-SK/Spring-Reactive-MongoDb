package com.Collage.Service;

import com.Collage.DTO.ResponseDTO;
import com.Collage.DTO.StudentDTO;
import com.Collage.DTO.StudentWithAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentService {
    Mono<ResponseEntity<ResponseDTO>> createStudent(StudentDTO studentDTO);
    Mono<Page<StudentDTO>> getAllStudentsWithPagination(Pageable pageable);
    Mono<ResponseEntity<ResponseDTO>> getStudentById(String id);
    Mono<ResponseEntity<ResponseDTO>> updateStudent(String id, StudentDTO studentDTO);
    Mono<ResponseEntity<ResponseDTO>> deleteStudent(String id);
    Mono<List<StudentWithAddressDTO>> getStudentsWithFullAddress();
    Mono<ResponseEntity<ResponseDTO>> getStudentsWithFullAddresss(Pageable pageable);
    Mono<ResponseEntity<ResponseDTO>> getStudentsByRegex(String pattern);

}