package org.helpdesk.posts.model.dto;

import org.helpdesk.posts.model.document.UserInfo;
import org.helpdesk.posts.util.EnumPostStates;

import java.util.List;

public class GetPostDto {
    private String postId;
    private EnumPostStates stateId;
    private UserInfo createdUserInfo;
    private UserInfo assignedUserInfo;
    private List<String> categoryList;
}
