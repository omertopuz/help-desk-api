package org.helpdesk.category.model.request;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CategoryDto {
    @NotEmpty
    private String categoryName;
    private List<CategoryDto> subCategoryList;

    public CategoryDto(@NotEmpty String categoryName, List<CategoryDto> subCategoryList) {
        this.categoryName = categoryName;
        this.subCategoryList = subCategoryList;
    }

    public CategoryDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<CategoryDto> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<CategoryDto> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

}
