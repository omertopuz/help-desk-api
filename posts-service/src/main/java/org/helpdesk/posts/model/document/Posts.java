package org.helpdesk.posts.model.document;

import org.helpdesk.posts.util.EnumPostStates;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("posts")
public class Posts extends AbstractDocument{
//    @Id
//    private String id;
    private String title;
    private String content;
    private String categoryId;

    private List<String> relatedPostIds;

    private List<String> assignedUsers;
    private List<Comments> comments;
    private List<Attachment> attachments;
    private List<PostHistory> historyList;

    public Posts() {

    }

    public Posts(String title,String content, String categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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

    public List<String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<PostHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<PostHistory> historyList) {
        this.historyList = historyList;
    }

    public List<String> getRelatedPostIds() {
        return relatedPostIds;
    }

    public void setRelatedPostIds(List<String> relatedPostIds) {
        this.relatedPostIds = relatedPostIds;
    }
}
