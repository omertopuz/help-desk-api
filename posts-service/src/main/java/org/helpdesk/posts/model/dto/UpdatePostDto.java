package org.helpdesk.posts.model.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdatePostDto {

    private String title;
    private String content;
    private String categoryId;

    public UpdatePostDto(String title, String content, String categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }

    public UpdatePostDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
