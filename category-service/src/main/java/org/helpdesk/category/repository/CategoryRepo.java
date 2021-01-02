package org.helpdesk.category.repository;

import org.helpdesk.category.model.document.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepo extends MongoRepository<Category,String> {

    List<Category> findByParentCategoryIdLike(String id);

//    @Query("{ 'parentCategoryId' : null }")
//    List<Category> findByParentCategoryIdNull();

    List<Category> findByParentCategoryIdIsNull();
    List<Category> deleteByParentCategoryIdLike(String parentCategoryId);
}
