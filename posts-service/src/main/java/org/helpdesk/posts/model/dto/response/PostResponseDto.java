package org.helpdesk.posts.model.dto.response;

import org.helpdesk.posts.model.document.PostHistory;

import java.util.List;

public class PostResponseDto extends AbstractResponse {

    private String title;
    private String content;
    private String categoryId;

    private List<String> relatedPostIds;

    private List<String> assignedUsers;
    private List<CommentResponseDto> comments;
    private List<AttachmentResponseDto> attachments;
    private List<PostHistory> historyList;

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

    public List<String> getRelatedPostIds() {
        return relatedPostIds;
    }

    public void setRelatedPostIds(List<String> relatedPostIds) {
        this.relatedPostIds = relatedPostIds;
    }

    public List<String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }

    public List<AttachmentResponseDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentResponseDto> attachments) {
        this.attachments = attachments;
    }

    public List<PostHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<PostHistory> historyList) {
        this.historyList = historyList;
    }
}
