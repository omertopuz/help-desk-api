package org.helpdesk.category.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.document.HierarchicalCategory;
import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.CategoryRequestDto;
import org.helpdesk.category.model.request.SubCategoryDto;
import org.helpdesk.category.model.request.UpdateCategoryDto;
import org.helpdesk.category.model.response.CategoryListResponseDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.service.CategoryService;
import org.helpdesk.category.service.FeignClientPostService;
import org.helpdesk.category.service.HierarchicalCategoryService;
import org.helpdesk.category.util.HierarchicalCategorySearchUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private FeignClientPostService feignClientPostService;

    private static ObjectMapper mapper;
    private static Logger log;

    @Autowired
    private CategoryService categoryService;

    private CategoryResponseDto testDataCategory;
    private static final String API_ENDPOINT = "/api/v2/categories";


    @BeforeAll
    public void setUp(){
        mapper = new ObjectMapper();
        log= LoggerFactory.getLogger(CategoryRestControllerTest.class);
        testDataCategory = getTestData();
    }

    private CategoryResponseDto getTestData(){
        return Optional.ofNullable(testDataCategory)
                .orElseGet(()->{
                    CategoryResponseDto mainCat = categoryService.createCategory(new CategoryRequestDto(null,"test-get-category-1"));
                    CategoryResponseDto sub_1 = categoryService.createCategory(new CategoryRequestDto(mainCat.getId(),"sub-category-1"));
                    CategoryResponseDto sub_1_1 = categoryService.createCategory(new CategoryRequestDto(sub_1.getId(),"sub-sub-category-1-1"));
                    CategoryResponseDto sub_1_2 = categoryService.createCategory(new CategoryRequestDto(sub_1.getId(),"sub-sub-category-1-2"));

                    CategoryResponseDto sub_2 = categoryService.createCategory(new CategoryRequestDto(mainCat.getId(),"sub-category-2"));

                    CategoryResponseDto sub_sub_2_1 = categoryService.createCategory(new CategoryRequestDto(sub_2.getId(),"sub-sub-category-2-1"));
                    CategoryResponseDto sub_sub_2_2 = categoryService.createCategory(new CategoryRequestDto(sub_2.getId(),"sub-sub-category-2-2"));
                    CategoryResponseDto sub_sub_sub_2_1_1 = categoryService.createCategory(new CategoryRequestDto(sub_sub_2_1.getId(),"sub-sub-sub-category-2-1-1"));

                    CategoryResponseDto sub_3 = categoryService.createCategory(new CategoryRequestDto(mainCat.getId(),"sub-category-3"));
                    CategoryResponseDto sub_sub_3_1 = categoryService.createCategory(new CategoryRequestDto(sub_3.getId(),"sub-sub-category-3-1"));
                    CategoryResponseDto sub_sub_3_2 = categoryService.createCategory(new CategoryRequestDto(sub_3.getId(),"sub-sub-category-3-2"));
                    CategoryResponseDto sub_sub_3_2_1 = categoryService.createCategory(new CategoryRequestDto(sub_sub_3_2.getId(),"sub-sub-sub-category-3-2-1"));
                    testDataCategory = categoryService.getCategoryIncludingSubCategories(mainCat.getId());
                    return testDataCategory;
                });

    }

    @Test
    public void createCategory_HttpStatusCreated_CategoryIdNotNull() throws Exception {
        CategoryDto request = new CategoryDto();
        request.setCategoryName("test-category-1");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT)
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json")
        )
        .andExpect(status().isCreated())
        .andReturn()
        ;

        CategoryResponseDto cat = getEntityFromResponseBody(result);
        assertThat(cat.getId()).isNotNull();
    }

    private CategoryResponseDto getEntityFromResponseBody(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.readValue(result.getResponse().getContentAsString(),CategoryResponseDto.class);
    }

    @Test
    public void getCategory_HttpStatusOk_CategoryIdAndSubcategoriesNotNull() throws Exception {
        String categoryId = getTestData().getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT+"/{categoryId}",categoryId))
                .andReturn();

        CategoryResponseDto cat = getEntityFromResponseBody(result);
        assertThat(cat.getId()).isNotNull();
        assertThat(cat.getSubCategoryList()).size().isGreaterThanOrEqualTo(1);
        assertThat(cat.getSubCategoryList().stream().findFirst().get().getId()).isNotNull();
    }

    @Test
    public void givenNotInsertedCategoryId_throwServiceException_CategoryNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT+"/{categoryId}", "123456"))
                .andExpect(result->assertEquals(
                        result.getResolvedException().getClass()
                        ,ServiceException.class))
                .andExpect(ex->assertThat(ex.getResolvedException().getMessage())
                        .contains("not found"));

    }

    @Test
    public void updateCategory_CheckNameEqualToRequestedName() throws Exception {
        String updatedCategoryName = "An updated category";
        CategoryDto request = new CategoryDto(updatedCategoryName,null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{categoryId}", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json")
        )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto cat = getEntityFromResponseBody(result);
        assertThat(cat.getCategoryName()).isEqualTo(updatedCategoryName);
    }

    @Test
    public void deleteCategory_HttpStatusOk_SearchGivenCategoryId_ThenGetServiceException_NotFound() throws Exception {
        CategoryResponseDto del = testDataCategory.getSubCategoryList().get(0);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{categoryId}", del.getId()))
                .andExpect(res->assertThat(res.getResponse().getContentAsString())
                        .contains("deleted"));

        Exception ex = assertThrows(ServiceException.class,()->{
            categoryService.getCategory(del.getId());
        });
        testDataCategory = null;//categoryService.getCategoryIncludingSubCategories(testDataCategory.getId());
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("not found");
    }

    @Test
    public void addSubCategory_SearchNewCategory_CheckSubCategoryListSizeGreaterThanOne() throws Exception {
        CategoryResponseDto parentNode = getTestData().getSubCategoryList().get(0);
        final String parentNodeId = parentNode.getId();
        int currentSubCategorySize = getTestData().getSubCategoryList().get(0).getSubCategoryList().size();
        String newCategoryName =  "sub-category-4";
        CategoryRequestDto request = new CategoryRequestDto(parentNode.getId(),newCategoryName);

        mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT)
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        parentNode = categoryService.getCategoryIncludingSubCategories(parentNodeId);

        assertThat(parentNode.getSubCategoryList()).hasSizeGreaterThanOrEqualTo(currentSubCategorySize+1);
        assertThat(parentNode.getSubCategoryList().stream().filter(p->p.getCategoryName().equals(newCategoryName))).isNotNull();
    }

    @Test
    public void updateSubCategory_ChangeParentCategoryOfCategoryAndName_CheckSubCategoryNameSetWRTRequest() throws Exception {
        CategoryResponseDto updateNode = getTestData().getSubCategoryList().get(0).getSubCategoryList().get(0);
        CategoryResponseDto newParentNode = getTestData().getSubCategoryList().get(0).getSubCategoryList().get(1);
        int currentSubCategorySize = newParentNode.getSubCategoryList().size();
        String newCategoryName =  "updated -" + updateNode.getCategoryName();
        CategoryRequestDto request = new CategoryRequestDto(newParentNode.getId(),newCategoryName);


        mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{categoryId}"
                        ,updateNode.getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
               ;

        CategoryListResponseDto allCat = categoryService.getAllCategories();
        newParentNode = categoryService.getCategoryIncludingSubCategories(newParentNode.getId());

        assertThat(newParentNode.getSubCategoryList()).hasSizeGreaterThanOrEqualTo(currentSubCategorySize+1);
    }

}
