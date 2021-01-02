package org.helpdesk.category.resource;

import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.request.CategoryDto;
import org.helpdesk.category.model.request.CategoryRequestDto;
import org.helpdesk.category.model.request.SubCategoryDto;
import org.helpdesk.category.model.request.UpdateCategoryDto;
import org.helpdesk.category.model.response.CategoryListResponseDto;
import org.helpdesk.category.model.response.CategoryResponseDto;
import org.helpdesk.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryListResponseDto> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable(name = "categoryId") String categoryId){
        return ResponseEntity.ok(categoryService.getCategoryIncludingSubCategories(categoryId));
    }

    @PutMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable(name = "categoryId") String categoryId,
                                                       @RequestBody CategoryRequestDto request){
        return ResponseEntity.ok(categoryService.updateCategory(categoryId,request));
    }

    @DeleteMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String>  deleteCategory(@PathVariable(name = "categoryId") String categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category " + categoryId + " has been deleted including subcategories");
    }


}
