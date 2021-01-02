package org.helpdesk.category.model.document;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Document(collection = "category")
public class Category{
    @Transient
    public static final String PATH_SEPARATOR = ".";

    @Id
    private String id;
    private String categoryName;
    private String parentCategoryId;

    @Transient
    private Collection<Category> subCategoryList = new HashSet<Category>();

    public Category() {

    }

    public Category(String categoryName, String parentCategoryId) {
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
    }

    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String lastModifiedBy;

    @CreatedDate
    protected Date creationDate;

    @LastModifiedDate
    protected Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Collection<Category> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(Collection<Category> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public Category setCustomParentCategoryId(Category parentCategory){
        String path = Optional.ofNullable(parentCategory.getParentCategoryId())
                .map(s1 -> String.format("%s%s%s",parentCategory.getParentCategoryId(),
                        parentCategory.getId(),Category.PATH_SEPARATOR))
                .orElseGet(() -> String.format("%s%s%s",Category.PATH_SEPARATOR,parentCategory.getId(),Category.PATH_SEPARATOR));
        this.setParentCategoryId(path);
        return this;
    }

}
