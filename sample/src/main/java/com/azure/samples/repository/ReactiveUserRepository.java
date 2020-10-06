package com.azure.samples.repository;

import com.azure.samples.models.User;
import com.azure.spring.data.cosmos.repository.Query;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import com.azure.backend.services.ICrudRepo;

@Repository
public interface ReactiveUserRepository extends ICrudRepo<User, String> {

    Flux<User> findByFirstName(String firstName);

    /**
     * Query for all documents
     **/
    @Query(value = "SELECT * FROM c")
    Flux<User> getAllUsers();

    @Query(value = "select * from c where c.id = @firstName and c.lastName = @lastName")
    Flux<User> getUsersByIdndLast(@Param("id") String id, @Param("lastName") String lastName);

    @Query(value = "select count(c.id) as num_ids, c.lastName from c group by c.lastName")
    Flux<ObjectNode> getUsersGroupByLastName();

}
