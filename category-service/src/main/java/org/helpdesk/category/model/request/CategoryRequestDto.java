package org.helpdesk.category.model.request;

public class CategoryRequestDto {

    private String parentCategoryId;

    private String categoryName;

    public CategoryRequestDto(String parentCategoryId, String categoryName) {
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

    public CategoryRequestDto() {
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
