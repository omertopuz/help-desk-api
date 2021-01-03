package org.helpdesk.posts.repository;

import org.bson.types.ObjectId;
import org.helpdesk.posts.model.document.Posts;
import org.helpdesk.posts.model.dto.response.AbstractResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends MongoRepository<Posts,String> {

    //TODO: define a query which ensures that the post should be created by the auditor or assigned to the auditor or auditor has privilege on category of post
    //@Query()
    Optional<Posts> findById(String postId);

    @Query(value = "{'categoryId':?0 }",fields = "{_id:1}")
    List<AbstractResponse> findByCategoryId(String categoryId);
}
