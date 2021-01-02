package org.helpdesk.posts.util;

import org.helpdesk.posts.exception.ServiceException;

import java.util.Arrays;

public enum EnumPostStates {
    POST_CREATED(1,"Issue created by user"),//("User create the post"),
    POST_UPDATED(2,"Issue updated by user"),//("User updated the post"),

    POST_VIEWED(3,"Issue viewed"),

    COMMENT_CREATED(4,"New comment added"),//("User add a comment"),
    COMMENT_UPDATED(5,"Comment updated"),//("User update the comment"),
    COMMENT_DELETED(6,"comment deleted"),//("User deleted the comment"),

    COMMENT_VIEWED(7,"Issue viewed"),

    ASSIGNED(8,"Issue assigned to authorized person"),//("post assigned to authorized person"),
    IN_PROCESS(9,"Issue has been processed"),//("authorized person deals with the post"),
    RESOLVED(10,"Issue has resolved"),
    CLOSED(11,"Issue has been closed"),
    ATTACHMENT_ADDED(12,"Attachment added"),
    ATTACHMENT_DOWNLOADED(13,"Attachment downloaded"),
    ATTACHMENT_DELETED(14,"Attachment deleted"),
    UNKNOWN(0,"Unknown state");

    private int postStateId;
    private String postStateDescription;

    EnumPostStates(int stateId,String desc) {
        this.postStateId = stateId;
        this.postStateDescription = desc;
    }

    public int stateId() {
        return postStateId;
    }
    public String stateDescription() {
        return postStateDescription;
    }
    public static EnumPostStates toEnumPostState(int stateId) {
        return Arrays.stream(EnumPostStates.values())
                .filter(p -> p.stateId() == stateId)
                .findFirst()
                .orElseThrow(()->new ServiceException("EnumPostStates casting error-> given state :" + stateId));
    }
}
