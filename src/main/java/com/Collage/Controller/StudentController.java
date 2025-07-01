package com.Collage.Controller;

import com.Collage.DTO.ResponseDTO;
import com.Collage.DTO.StudentDTO;
import com.Collage.PageRequest.PageRequestPayload;
import com.Collage.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing Student entities.
 * Provides endpoints for creating, reading, updating, and deleting students.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    /**
     * Creates a new student.
     *
     * @param studentDTO The student data to be created.
     * @return A Mono containing a ResponseEntity with ResponseDTO indicating the operation result.
     */
    @PostMapping("/save")
    public Mono<ResponseEntity<ResponseDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    /**
     * Retrieves a paginated list of all students.
     *
     * @param pageRequestPayload Contains page number, size, and sort parameters.
     * @return A Mono containing a Page of StudentDTO.
     */
    @GetMapping("/getAll")
    public Mono<Page<StudentDTO>> getAllStudents(PageRequestPayload pageRequestPayload) {
        Pageable pageable = pageRequestPayload.getPageable();
        return studentService.getAllStudentsWithPagination(pageable);
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id The ID of the student to retrieve.
     * @return A Mono containing a ResponseEntity with ResponseDTO and student data if found.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id);
    }

    /**
     * Updates an existing student by ID.
     *
     * @param id         The ID of the student to update.
     * @param studentDTO The updated student data.
     * @return A Mono containing a ResponseEntity with ResponseDTO indicating the result of the update.
     */
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<ResponseDTO>> updateStudent(@PathVariable String id, @Valid @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }

    /**
     * Deletes a student by their ID.
     *
     * @param id The ID of the student to delete.
     * @return A Mono containing a ResponseEntity with ResponseDTO indicating the deletion result.
     */
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<ResponseDTO>> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/with-address")
    public Mono<ResponseEntity<ResponseDTO>> getAllWithAddress() {
        return studentService.getStudentsWithFullAddress()
                .map(list -> ResponseEntity.ok(new ResponseDTO(200, true, "Students with addresses fetched", list)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(500, false, "Aggregation failed"))));
    }

/**
 * Performs an aggregation pipeline to fetch students along with their full address details.
 *
 * <p>The pipeline includes the following stages:</p>
 *
 * <ul>
 *     <li><b>$lookup</b>: Performs a left outer join with the "address" collection
 *         on "addressId" in student matching "_id" in address, outputting as "addresses".</li>
 *     <li><b>$sort</b>: Sorts the result by the specified field (e.g., "name") in ascending or descending order.</li>
 *     <li><b>$skip</b>: Skips (pageNo * pageSize) number of documents for pagination.</li>
 *     <li><b>$limit</b>: Limits the number of returned documents to the specified page size.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * Aggregation aggregation = Aggregation.newAggregation(
 *     Aggregation.lookup("address", "addressId", "_id", "addresses"),
 *     Aggregation.sort(Sort.by(Sort.Direction.ASC, "name")),
 *     Aggregation.skip((long) pageNo * pageSize),
 *     Aggregation.limit(pageSize)
 * );
 * }</pre>
 *
 * @return a paginated list of students, each enriched with their related address data.
 *  */
    @GetMapping("/with-addresss")
    public Mono<ResponseEntity<ResponseDTO>> getAllWithAddresss(PageRequestPayload pageRequestPayload) {
        Pageable pageable = pageRequestPayload.getPageable();
        return studentService. getStudentsWithFullAddresss(pageable);
    }

    @GetMapping("/by-pattern/{pattern}")
    public Mono<ResponseEntity<ResponseDTO>> getByRegex(@PathVariable String pattern) {
        return studentService.getStudentsByRegex(pattern);
    }



}
