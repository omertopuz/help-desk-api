package org.helpdesk.category.repository;

import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.document.HierarchicalCategory;
import org.helpdesk.category.model.request.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.Revision;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class HierarchicalCategoryRepoTest {

    @Autowired
    private HierarchicalCategoryRepo categoryRepo;

    @Test
    public void subCategoryTest(){
        HierarchicalCategory request = new HierarchicalCategory("test-get-category-1",
                Stream.of(new HierarchicalCategory("sub-category-1",
                        Stream.of(new HierarchicalCategory("sub-sub-category-1",null))
                                .collect(Collectors.toSet())))
                        .collect(Collectors.toSet()));
        HierarchicalCategory entity = categoryRepo.save(request);

        String subId = entity.getSubCategoryList().stream().collect(Collectors.toList()).get(0)
                .getSubCategoryList().stream().collect(Collectors.toList()).get(0).getId();


    }

}
