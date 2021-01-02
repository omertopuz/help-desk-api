package org.helpdesk.category.model.request;

import java.util.Set;

public class SubCategoryDto {

    private String parentCategoryId;
    private Set<CategoryDto> subCategoryList;

    public SubCategoryDto(String parentCategoryId, Set<CategoryDto> subCategoryList) {
        this.parentCategoryId = parentCategoryId;
        this.subCategoryList = subCategoryList;
    }

    public SubCategoryDto() {
    }

    public Set<CategoryDto> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(Set<CategoryDto> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
