package org.helpdesk.posts.model.dto.response;

import java.util.List;

public class PostIdListInCategoryResponse {
    private List<AbstractResponse> postIdList;

    public PostIdListInCategoryResponse() {
    }

    public PostIdListInCategoryResponse(List<AbstractResponse> postIdList) {
        this.postIdList = postIdList;
    }

    public List<AbstractResponse> getPostIdList() {
        return postIdList;
    }

    public void setPostIdList(List<AbstractResponse> postIdList) {
        this.postIdList = postIdList;
    }
}
