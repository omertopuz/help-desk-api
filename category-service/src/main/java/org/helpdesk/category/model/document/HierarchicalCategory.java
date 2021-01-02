package org.helpdesk.category.model.document;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Document(collection = "hierarchical-category")
public class HierarchicalCategory{
    @Id
    protected String id;
    protected String categoryName;

    protected Set<HierarchicalCategory> subCategoryList;

    public HierarchicalCategory() {
    }

    public HierarchicalCategory(String categoryName, Set<HierarchicalCategory> subCategoryList) {
        this.categoryName = categoryName;
        this.subCategoryList = subCategoryList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<HierarchicalCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(Set<HierarchicalCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }


    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String lastModifiedBy;

    @CreatedDate
    protected Date creationDate;

    @LastModifiedDate
    protected Date updateDate;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
