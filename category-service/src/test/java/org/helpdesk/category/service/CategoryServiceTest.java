package org.helpdesk.category.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.internal.bytebuddy.matcher.ElementMatchers;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.CategoryRequestDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.model.response.PostIdListInCategoryResponse;
import org.helpdesk.category.model.response.PostServiceIdListResponse;
import org.helpdesk.category.repository.CategoryRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {

    private static ObjectMapper mapper;
    private static Logger log;

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private FeignClientPostService feignClientPostService;

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

                    request = request.setCustomParentCategoryId(request);
                    request.setCategoryName("sub-category-1");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request.setCategoryName("sub-category-2");
                    request.setId(null);
                    categoryRepo.insert(request);

                    Category newRequest = new Category("sub-sub-category-2-1",null);
                    newRequest.setCustomParentCategoryId(request);
                    categoryRepo.insert(newRequest);

                    Category newNewRequest = new Category("sub-sub-sub-category-2-1-1",null);
                    newNewRequest = newNewRequest.setCustomParentCategoryId(newRequest);
                    categoryRepo.insert(newNewRequest);

                    newRequest.setCategoryName("sub-sub-category-2-2");
                    newRequest.setId(null);
                    categoryRepo.insert(newRequest);

                    request.setCategoryName("sub-category-3");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request = request.setCustomParentCategoryId(request);
                    request.setCategoryName("sub-sub-category-3-1");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request.setCategoryName("sub-sub-category-3-2");
                    request.setId(null);
                    categoryRepo.insert(request);

                    request = request.setCustomParentCategoryId(request);
                    request.setCategoryName("sub-sub-sub-category-3-2-1");
                    request.setId(null);
                    categoryRepo.insert(request);

                    testDataCategory = categoryRepo.findAll();
                    return testDataCategory;
                });

    }

    @Test
    public void getCategoryIncludingSubCategoriesHierarchicalWay(){
        Category mainCategory = testDataCategory.stream().filter(p->p.getParentCategoryId()==null).findFirst().get();
        CategoryResponseDto mainCategoryHierarchical = categoryService.getCategoryIncludingSubCategories(mainCategory.getId());

        Category sub = testDataCategory.stream().filter(p->p.getParentCategoryId() != null).findFirst().get();
        Category subSub = testDataCategory.stream().filter(p->p.getCategoryName().startsWith("sub-sub-c")).findFirst().get();
        Category subSubSub = testDataCategory.stream().filter(p->p.getCategoryName().startsWith("sub-sub-sub-c")).findFirst().get();

        assertThat(mainCategoryHierarchical.getSubCategoryList()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(mainCategoryHierarchical.getSubCategoryList().stream().filter(p->p.getCategoryName().equals(sub.getCategoryName()))).isNotNull();
        assertThat(mainCategoryHierarchical.getSubCategoryList().stream().filter(p->p.getSubCategoryList().stream().filter(q->q.getCategoryName().equals(subSub.getCategoryName())).findFirst().isPresent())).isNotNull();
        assertThat(mainCategoryHierarchical.getSubCategoryList().stream().filter(p->p.getSubCategoryList().stream().filter(q->q.getSubCategoryList().stream().filter(w->w.getCategoryName().equals(subSubSub.getCategoryName())).findFirst().isPresent()).findFirst().isPresent())).isNotNull();
    }

    @Test
    public void getAllMainCategories_ExpectOnlyOneRecord(){
        List<CategoryResponseDto> mainCategories = categoryService.getAllMainCategories();
        assertThat(mainCategories).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    public void deleteCategory_InCaseNoPostBelongToCategory_ThenExpectSuccessMessage(){
        String categoryId = getTestData().get(0).getId();

        Mockito.when(feignClientPostService.getPostsWithGivenCategoryId(ArgumentMatchers.anyString()))
                .thenReturn(new PostIdListInCategoryResponse());
        categoryService.deleteCategory(categoryId);

        Exception ex = assertThrows(ServiceException.class,()->{
            categoryService.getCategory(categoryId);
        });
        testDataCategory = null;
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("not found");
    }

    @Test
    public void deleteCategory_InCasePostBelongToCategory_ThenExceptionThrown(){
        String categoryId = getTestData().get(0).getId();

        Mockito.when(feignClientPostService.getPostsWithGivenCategoryId(ArgumentMatchers.anyString()))
                .thenReturn(new PostIdListInCategoryResponse(
                        Stream.of(new PostServiceIdListResponse(categoryId))
                                .collect(Collectors.toList())
                ));

        Exception ex = assertThrows(ServiceException.class,()->{
            categoryService.deleteCategory(categoryId);
        });

        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("Can not be deleted");
    }
}
