package org.helpdesk.posts.model.dto;

public class CommentDto{
    private String comment;

    public CommentDto() {
    }

    public CommentDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
