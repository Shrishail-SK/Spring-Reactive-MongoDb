package com.Collage.Repository;

import com.Collage.Entity.Address;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends ReactiveMongoRepository<Address, String> {
}
