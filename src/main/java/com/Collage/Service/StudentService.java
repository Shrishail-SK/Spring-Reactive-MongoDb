package com.Collage.Service;

import com.Collage.DTO.ResponseDTO;
import com.Collage.DTO.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface StudentService {
    Mono<ResponseEntity<ResponseDTO>> createStudent(StudentDTO studentDTO);
    Mono<Page<StudentDTO>> getAllStudentsWithPagination(Pageable pageable);
    Mono<ResponseEntity<ResponseDTO>> getStudentById(String id);
    Mono<ResponseEntity<ResponseDTO>> updateStudent(String id, StudentDTO studentDTO);
    Mono<ResponseEntity<ResponseDTO>> deleteStudent(String id);

}