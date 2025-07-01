package com.Collage.Repository;

import com.Collage.DTO.StudentWithAddressDTO;
import com.Collage.Entity.Student;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface StudentRepo extends ReactiveMongoRepository<Student, String> {
    // @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    @Aggregation(pipeline = {
            "{ $match: { name: { $regex: ?0, $options: 'i' } } }",
            "{ $unwind: { path: '$addressId', preserveNullAndEmptyArrays: true } }",
            "{ $unwind: { path: '$hobbiesId', preserveNullAndEmptyArrays: true } }",
            "{ $lookup: { from: 'address', localField: 'addressId', foreignField: '_id', as: 'addressDetails' } }",
            "{ $lookup: { from: 'hobbies', localField: 'hobbiesId', foreignField: '_id', as: 'hobbiesDetails' } }",
            "{ $unwind: { path: '$addressDetails', preserveNullAndEmptyArrays: true } }",
            "{ $unwind: { path: '$hobbiesDetails', preserveNullAndEmptyArrays: true } }",
            "{ $group: { " +
                    "_id: '$_id', " +
                    "name: { $first: '$name' }, " +
                    "age: { $first: '$age' }, " +
                    "department: { $first: '$department' }, " +
                    "phone: { $first: '$phone' }, " +
                    "email: { $first: '$email' }, " +
                    "feesPaid: { $first: '$feesPaid' }, " +
                    "addressDetails: { $addToSet: '$addressDetails' }, " +
                    "hobbiesDetails: { $addToSet: '$hobbiesDetails' }" +
                    "} }",
            "{ $project: { " +
                    "_id: 1, name: 1, age: 1, department: 1, phone: 1, email: 1, feesPaid: 1, addressDetails: 1, hobbiesDetails: 1 " +
                    "} }"
    })

    Flux<StudentWithAddressDTO> findByNamePattern(String pattern);
}
