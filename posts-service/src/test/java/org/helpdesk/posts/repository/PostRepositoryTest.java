package org.helpdesk.posts.repository;

import org.assertj.core.api.Condition;
import org.helpdesk.posts.model.document.Attachment;
import org.helpdesk.posts.model.document.Comments;
import org.helpdesk.posts.model.document.Posts;
import org.helpdesk.posts.model.dto.response.AbstractResponse;
import org.helpdesk.posts.util.EnumPostStates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

    @Autowired
    private PostRepo postRepo;

    private List<Posts> testDataPost;

    @BeforeAll
    public void setUp(){
        testDataPost = getTestData();
    }

    private List<Posts> getTestData(){
        return Optional.ofNullable(testDataPost)
                .orElseGet(()-> {
                    Posts request = new Posts("title: 1", "content -1"
                            , "1");
                    request.setCurrentEvent(EnumPostStates.POST_CREATED);
                    request.setStateId(EnumPostStates.POST_CREATED.stateId());
                    request.setAttachments(Stream.of(new Attachment("1123", "q-file-1.pdf")
                            , new Attachment("1127", "q-2-file.jpg")
                            , new Attachment("1128", "file-3.txt"))
                            .collect(Collectors.toList()));

                    Posts p = postRepo.insert(request);
                    p.setId(null);
                    p.setTitle("title-2");
                    p.setCategoryId("2");
                    p.setContent("content-2");
                    p = postRepo.insert(request);
                    p.setId(null);
                    p.setTitle("title-3");
                    p.setCategoryId("3");
                    p.setContent("content-3");
                    p = postRepo.insert(request);
                    testDataPost = postRepo.findAll();
                    return testDataPost;
                });
    }

    @Test
    public void insertPost_Check_PostId_CreatedBy_Comments_Attachments_PostHistory_NotNull(){
        Posts request = new Posts("title: createPost","createPost_HttpStatusCreated_PostIdNotNull"
                ,"5fecec004ef1572eec2cb72e");
        request.setCurrentEvent(EnumPostStates.POST_CREATED);
        request.setStateId(EnumPostStates.POST_CREATED.stateId());
        request.setAttachments(Stream.of(new Attachment("1123","q-file-1.pdf")
                ,new Attachment("1127","q-2-file.jpg")
                ,new Attachment("1128","file-3.txt"))
                .collect(Collectors.toList()));

        Posts p = postRepo.insert(request);

        assertThat(p.getId()).isNotNull();
        assertThat(p.getCreatedBy()).isNotNull();
        assertThat(p.getAttachments()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getAttachments().get(0).getCreatedBy()).isNotNull();
        assertThat(p.getHistoryList()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getHistoryList()).haveAtLeast(1,new Condition<>(
                m -> m.getCurrentEvent()== EnumPostStates.POST_CREATED,
                "POST_CREATED HISTORY"
        ));

        Comments c = new Comments();
        c.setComment("some comment");
        c.setStateId(EnumPostStates.COMMENT_CREATED.stateId());
        request.setComments(Stream.of(c).collect(Collectors.toList()));
        request.setCurrentEvent(EnumPostStates.COMMENT_CREATED);
        p = postRepo.save(request);

        assertThat(p.getComments()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getComments().get(0).getCreatedBy()).isNotNull();
        assertThat(p.getHistoryList()).haveAtLeast(1,new Condition<>(
                m -> m.getCurrentEvent()== EnumPostStates.COMMENT_CREATED,
                "COMMENT_CREATED HISTORY"
        ));
    }

    @Test
    public void test_findByCategoryId(){
        Posts p = getTestData().get(0);
        String categoryId = p.getCategoryId();
        int currentCountWRTGivenCategoryId = (int) getTestData().stream().filter(c->c.getCategoryId().equals(categoryId)).count();
        List<AbstractResponse> result = postRepo.findByCategoryId(categoryId);

        assertThat(result).hasSize(currentCountWRTGivenCategoryId);
        assertThat(result.get(0).getId()).isEqualTo(p.getId());
    }
}
