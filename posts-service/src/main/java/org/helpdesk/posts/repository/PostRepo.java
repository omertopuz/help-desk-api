package org.helpdesk.posts.repository;

import org.helpdesk.posts.model.document.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PostRepo extends MongoRepository<Posts,String> {

    //TODO: define a query which ensures that the post should be created by the auditor or assigned to the auditor or auditor has privilege on category of post
    //@Query()
    Optional<Posts> findById(String postId);
}
