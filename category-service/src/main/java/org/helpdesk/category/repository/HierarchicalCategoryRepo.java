package org.helpdesk.category.repository;

import org.helpdesk.category.model.document.HierarchicalCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HierarchicalCategoryRepo extends MongoRepository<HierarchicalCategory,String> {

}
