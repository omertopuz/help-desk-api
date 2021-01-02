package org.helpdesk.posts.model.dto.response;

public class CommentResponseDto extends AbstractResponse {
    private String comment;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
