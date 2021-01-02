package org.helpdesk.category.model.request;

public class UpdateCategoryDto {

    private String categoryName;

    public UpdateCategoryDto() {
    }

    public UpdateCategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
