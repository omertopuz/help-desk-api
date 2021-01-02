package org.helpdesk.category.resource;

import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.SubCategoryDto;
import org.helpdesk.category.model.request.UpdateCategoryDto;
import org.helpdesk.category.model.response.CategoryListResponseDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.service.CategoryService;
import org.helpdesk.category.service.HierarchicalCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Deprecated
@RestController
@RequestMapping("/api")
public class HierarchicalCategoryResource {

    @Autowired
    private HierarchicalCategoryService categoryService;

    @GetMapping(value = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryListResponseDto> getAllCategories(){
        return ResponseEntity.ok(new CategoryListResponseDto(categoryService.getAllCategories()));
    }

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryResponseDto>  createCategory(@RequestBody CategoryDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto>  getCategory(@PathVariable(name = "categoryId") String categoryId){
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @PutMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto>  updateCategory(@PathVariable(name = "categoryId") String categoryId,
                                                       @RequestBody CategoryDto request){
        return ResponseEntity.ok(categoryService.updateCategory(categoryId,request));
    }

    @DeleteMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String>  deleteCategory(@PathVariable(name = "categoryId") String categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category " + categoryId + " has been deleted");
    }

    @PostMapping(value = "/categories/{categoryId}/subcategories")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto> createSubCategory(@PathVariable(name = "categoryId") String categoryId,@RequestBody SubCategoryDto request){
        return ResponseEntity.ok(categoryService.createSubCategory(categoryId,request));
    }

    @PutMapping(value = "/categories/{categoryId}/subcategories/{subCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto> updateSubCategory(@PathVariable(name = "categoryId") String categoryId,
                                                       @PathVariable(name = "subCategoryId") String subCategoryId,
                                                       @RequestBody UpdateCategoryDto request){
        return ResponseEntity.ok(categoryService.updateSubCategory(categoryId,subCategoryId,request));
    }

    @DeleteMapping(value = "/categories/{categoryId}/subcategories/{subCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto> deleteSubCategory(@PathVariable(name = "categoryId") String categoryId,
                                                       @PathVariable(name = "subCategoryId") String subCategoryId){
        return ResponseEntity.ok(categoryService.deleteSubCategory(categoryId,subCategoryId));
    }

}
