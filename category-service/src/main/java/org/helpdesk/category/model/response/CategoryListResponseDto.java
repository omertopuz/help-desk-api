package org.helpdesk.category.model.response;

import java.util.List;

public class CategoryListResponseDto {

    private List<CategoryResponseDto> categoryList;

    public CategoryListResponseDto(List<CategoryResponseDto> categoryList) {
        this.categoryList = categoryList;
    }

    public List<CategoryResponseDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryResponseDto> categoryList) {
        this.categoryList = categoryList;
    }
}
