package org.helpdesk.posts.model.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NewPostDto {

    @NotNull(message = "title must be filled")
    private String title;
    @NotNull(message = "content must be filled")
    private String content;
    @NotNull(message = "categoryId must be filled")
    private String categoryId;
    private List<AttachmentDto> attachments;

    public NewPostDto(String title, String content, String categoryId, List<AttachmentDto> attachments) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.attachments = attachments;
    }

    public NewPostDto() {
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

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

}
