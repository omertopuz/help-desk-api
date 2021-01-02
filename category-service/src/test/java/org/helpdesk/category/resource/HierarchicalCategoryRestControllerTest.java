package org.helpdesk.category.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.category.exception.ServiceException;
import org.helpdesk.category.model.document.HierarchicalCategory;
import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.SubCategoryDto;
import org.helpdesk.category.model.request.UpdateCategoryDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.service.HierarchicalCategoryService;
import org.helpdesk.category.util.HierarchicalCategorySearchUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
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
class HierarchicalCategoryRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    private static ObjectMapper mapper;
    private static Logger log;

    @Autowired
    private HierarchicalCategoryService categoryService;

    private CategoryResponseDto testDataCategory;
    private static final String API_ENDPOINT = "/api/categories";


    @BeforeAll
    public void setUp(){
        mapper = new ObjectMapper();
        log= LoggerFactory.getLogger(HierarchicalCategoryRestControllerTest.class);
        testDataCategory = getTestData();
    }

    private CategoryResponseDto getTestData(){
        return Optional.ofNullable(testDataCategory)
                .orElseGet(()->{
                    CategoryDto request = new CategoryDto("test-get-category-1",
                            Stream.of(new CategoryDto("sub-category-1",
                                    Stream.of(new CategoryDto("sub-sub-category-1",null))
                                            .collect(Collectors.toList())))
                                    .collect(Collectors.toList()));
                    testDataCategory = categoryService.createCategory(request);
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

        HierarchicalCategory cat = getEntityFromResponseBody(result);
        assertThat(cat.getId()).isNotNull();
    }

    private HierarchicalCategory getEntityFromResponseBody(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.readValue(result.getResponse().getContentAsString(),HierarchicalCategory.class);
    }

    @Test
    public void getCategory_HttpStatusOk_CategoryIdAndSubcategoriesNotNull() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT+"/{categoryId}", getTestData().getId()))
                .andReturn();

        HierarchicalCategory cat = getEntityFromResponseBody(result);
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

        HierarchicalCategory cat = getEntityFromResponseBody(result);
        assertThat(cat.getCategoryName()).isEqualTo(updatedCategoryName);
    }

    @Test
    public void deleteCategory_HttpStatusOk_SearchGivenCategoryId_ThenGetServiceException_NotFound() throws Exception {

        String categoryId = getTestData().getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{categoryId}", getTestData().getId()))
                .andExpect(res->assertThat(res.getResponse().getContentAsString())
                        .contains("deleted"));

        Exception ex = assertThrows(ServiceException.class,()->{
            categoryService.getCategory(categoryId);
        });
        testDataCategory = null;
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("not found");
    }

    @Test
    public void addSubCategory_SearchNewCategory_CheckSubCategoryListSizeGreaterThanOne() throws Exception {
        String parentNodeId = getTestData().getSubCategoryList().get(0).getId();
        String newCategoryName =  "sub-sub-category-2";
        SubCategoryDto request = new SubCategoryDto(parentNodeId,
                Stream.of(new CategoryDto(newCategoryName,null))
                        .collect(Collectors.toSet()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT+"/{categoryId}/subcategories", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        HierarchicalCategory hCat = getEntityFromResponseBody(result);

        HierarchicalCategory parentOfNewCategory= HierarchicalCategorySearchUtil.searchSubNode(hCat,parentNodeId);
        Optional<HierarchicalCategory> newCategoryEntity = parentOfNewCategory.getSubCategoryList()
                .stream()
                .filter(c->newCategoryName.equals(c.getCategoryName())).findFirst();

        assertThat(parentOfNewCategory.getSubCategoryList()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(newCategoryEntity.isPresent()).isTrue();
        assertThat(newCategoryEntity.get().getCategoryName()).isEqualTo(newCategoryName);
    }

    @Test
    public void updateSubCategory_CheckSubCategoryNameSetWRTRequest() throws Exception {
        String subCategoryId = getTestData().getSubCategoryList().get(0).getSubCategoryList().get(0).getId();
        String updateCategoryName =  "updated - sub-sub-category-2";

        UpdateCategoryDto request = new UpdateCategoryDto(updateCategoryName);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{categoryId}/subcategories/{subCategoryId}"
                        ,getTestData().getId(),subCategoryId)
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        HierarchicalCategory hCat = getEntityFromResponseBody(result);

        HierarchicalCategory subCategory= HierarchicalCategorySearchUtil.searchSubNode(hCat,subCategoryId);

        assertThat(subCategory.getCategoryName()).isEqualTo(updateCategoryName);
    }

    @Test
    public void deleteSubCategory_SearchInSubCategories_ThenExpectNotFoundException() throws Exception {
        String subCategoryId = getTestData().getSubCategoryList().get(0).getSubCategoryList().get(0).getId();
        String updateCategoryName =  "updated - sub-sub-category-2";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{categoryId}/subcategories/{subCategoryId}"
                        ,getTestData().getId(),subCategoryId)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        HierarchicalCategory hCat = getEntityFromResponseBody(result);

        Exception ex = assertThrows(ServiceException.class,()->{
            HierarchicalCategorySearchUtil.searchSubNode(hCat,subCategoryId);
        });
        testDataCategory = null;
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("not found");
    }

}
