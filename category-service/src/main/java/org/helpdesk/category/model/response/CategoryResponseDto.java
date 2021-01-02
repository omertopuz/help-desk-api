package org.helpdesk.category.model.response;

import java.util.List;

public class CategoryResponseDto {
    private String id;
    private String categoryName;
    private List<CategoryResponseDto> subCategoryList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<CategoryResponseDto> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<CategoryResponseDto> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
