package org.helpdesk.category.service;

import org.helpdesk.category.model.response.PostIdListInCategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${open-feign-clients.post-service.base-url}", name = "${open-feign-clients.post-service.name}")
public interface FeignClientPostService {

    @GetMapping("${open-feign-clients.post-service.methods.get-posts-with-given-category-id}")
    public PostIdListInCategoryResponse getPostsWithGivenCategoryId(@RequestParam(name = "categoryId") String categoryId);
}
