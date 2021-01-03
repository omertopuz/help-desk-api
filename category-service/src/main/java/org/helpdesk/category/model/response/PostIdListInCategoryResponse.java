package org.helpdesk.category.model.response;

import java.util.List;

public class PostIdListInCategoryResponse {
    private List<PostServiceIdListResponse> postIdList;

    public PostIdListInCategoryResponse() {
    }

    public PostIdListInCategoryResponse(List<PostServiceIdListResponse> postIdList) {
        this.postIdList = postIdList;
    }

    public List<PostServiceIdListResponse> getPostIdList() {
        return postIdList;
    }

    public void setPostIdList(List<PostServiceIdListResponse> postIdList) {
        this.postIdList = postIdList;
    }
}
