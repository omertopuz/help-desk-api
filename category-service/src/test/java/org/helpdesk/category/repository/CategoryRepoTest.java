package org.helpdesk.category.repository;

import org.assertj.core.api.Condition;
import org.helpdesk.category.model.document.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRepoTest {

    @Autowired
    private CategoryRepo categoryRepo;
    private List<Category> testDataCategory;
    @BeforeAll
    public void setUp(){
        testDataCategory = getTestData();
    }

    private List<Category> getTestData(){
        return Optional.ofNullable(testDataCategory)
                .orElseGet(()->{
                    Category request = new Category("test-get-category-1",null);
                    Category mainCategory = categoryRepo.insert(request);

                    assertThat(mainCategory.getId()).isNotNull();

                    request = request.setCustomParentCategoryId(request);
                    request.setCategoryName("sub-category-1");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request.setCategoryName("sub-category-2");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request.setCategoryName("sub-category-3");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request = request.setCustomParentCategoryId(request);
                    request.setCategoryName("sub-sub-category-1");
                    request.setId(null);
                    categoryRepo.insert(request);
                    testDataCategory = categoryRepo.findAll();
                    return testDataCategory;
                });

    }

    @Test
    public void createMainCategory_checkIdNotNull_Then_CreateSubCategory_CheckPath(){
        Category request = new Category("test-get-category-1",null);
        Category mainCategory = categoryRepo.save(request);

        assertThat(mainCategory.getId()).isNotNull();

        request = request.setCustomParentCategoryId(request);
        Category subCategory = categoryRepo.save(request);

        assertThat(subCategory.getParentCategoryId()).isEqualTo("."+mainCategory.getId()+".");
    }

    @Test
    public void getCategoryIncludingSubCategoriesTest(){
        Category mainCategory = testDataCategory.stream().filter(p->p.getParentCategoryId()==null).findFirst().get();
        List<Category> subCategoryList = categoryRepo.findByParentCategoryIdLike(mainCategory.getId());

        assertThat(subCategoryList).hasSizeGreaterThanOrEqualTo(1);
        assertThat(subCategoryList).haveAtLeast(1,new Condition<>(
                m ->("."+mainCategory.getId()+".").equals(m.getParentCategoryId()),
                "PARENT_CATEGORY_PATH"
        ));
    }

    @Test
    public void getCategories_WhereParentCategoryIdIsNull_ExpectOnlyOneRecord(){
        List<Category> mainCategories = categoryRepo.findByParentCategoryIdIsNull();
        assertThat(mainCategories).hasSizeGreaterThanOrEqualTo(1);
    }
}
