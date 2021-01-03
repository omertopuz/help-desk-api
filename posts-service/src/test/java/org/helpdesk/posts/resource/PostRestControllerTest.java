package org.helpdesk.posts.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.posts.exception.ServiceException;
import org.helpdesk.posts.model.document.Posts;
import org.helpdesk.posts.model.dto.AttachmentDto;
import org.helpdesk.posts.model.dto.CommentDto;
import org.helpdesk.posts.model.dto.NewPostDto;
import org.helpdesk.posts.model.dto.UpdatePostDto;
import org.helpdesk.posts.model.dto.response.AttachmentResponseDto;
import org.helpdesk.posts.model.dto.response.CommentResponseDto;
import org.helpdesk.posts.model.dto.response.PostIdListInCategoryResponse;
import org.helpdesk.posts.model.dto.response.PostResponseDto;
import org.helpdesk.posts.service.PostsService;
import org.helpdesk.posts.util.EnumPostStates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    private static ObjectMapper mapper;
    private static Logger log;

    @Autowired
    private PostsService postsService;

    private PostResponseDto testDataPost;
    private static final String API_ENDPOINT = "/api/posts";


    @BeforeAll
    public void setUp(){
        mapper = new ObjectMapper();
        log= LoggerFactory.getLogger(PostRestControllerTest.class);
        testDataPost = getTestData();
    }

    private PostResponseDto getTestData(){
        return Optional.ofNullable(testDataPost)
                .orElseGet(()->{
                    NewPostDto request = new NewPostDto("hey! commander, something strange coming towards us"
                            ,"It is likely to hit us. should be stopped before reaching "
                            ,"5"
                            ,Stream.of(new AttachmentDto("1123","q-file-1.pdf")
                    ,new AttachmentDto("1127","q-2-file.jpg")
                    ,new AttachmentDto("1128","file-3.txt"))
                            .collect(Collectors.toList())
                    );

                    testDataPost = postsService.createNewPost(request);
                    return testDataPost;
                });

    }

    @Test
    public void createPost_HttpStatusCreated_PostIdNotNull() throws Exception {
        NewPostDto request = new NewPostDto("title: createPost","createPost_HttpStatusCreated_PostIdNotNull"
                ,"5fecec004ef1572eec2cb72e"
                ,Stream.of(new AttachmentDto("1123","q-file-1.pdf")
                ,new AttachmentDto("1127","q-2-file.jpg")
                ,new AttachmentDto("1128","file-3.txt"))
                .collect(Collectors.toList())
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT)
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json")
        )
        .andExpect(status().isCreated())
        .andReturn()
        ;

        PostResponseDto p = mapper.readValue(result.getResponse().getContentAsString(),PostResponseDto.class);
        assertThat(p.getId()).isNotNull();
        assertThat(p.getStateEnum() == EnumPostStates.POST_CREATED);
        assertThat(p.getAttachments()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getAttachments().get(0).getId()).isNotNull();
    }

    @Test
    public void getPost_HttpStatusOk_PostIdAndAttachmentsNotNull() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT+"/{postId}", getTestData().getId()))
                .andReturn();

        PostResponseDto p = mapper.readValue(result.getResponse().getContentAsString(),PostResponseDto.class);
        assertThat(p.getId()).isNotNull();
        assertThat(p.getAttachments()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getAttachments().get(0).getId()).isNotNull();
    }

    @Test
    public void givenNotInsertedPostId_throwServiceException_PostNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT+"/{postId}", "123456"))
                .andExpect(result->assertEquals(
                        result.getResolvedException().getClass()
                        , ServiceException.class))
                .andExpect(ex->assertThat(ex.getResolvedException().getMessage())
                        .contains("not found"));

    }

    @Test
    public void updatePost_CheckTitleAndContentAndCategoryIdEqualToRequest() throws Exception {
        String updatedPostTitle = getTestData().getTitle() + " -> title updated";
        String updatedPostContent = getTestData().getContent() + " -> content updated";
        String updatedCategoryId = "5fecec004ef1572eec2cb72c";

        UpdatePostDto request = new UpdatePostDto(updatedPostTitle,updatedPostContent,updatedCategoryId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{postId}", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json")
        )
                .andExpect(status().isOk())
                .andReturn();

        PostResponseDto p = mapper.readValue(result.getResponse().getContentAsString(),PostResponseDto.class);
        assertThat(p.getTitle()).isEqualTo(updatedPostTitle);
        assertThat(p.getContent()).isEqualTo(updatedPostContent);
        assertThat(p.getCategoryId()).isEqualTo(updatedCategoryId);
    }

    @Test
    public void updatePost_makeRequestForJustCategoryId_CheckTitleAndContentNotChanged_CategoryIdEqualToRequest() throws Exception {
        testDataPost = null;
        String updatedCategoryId = "5fecec004ef1572eec2cb72c";

        UpdatePostDto request = new UpdatePostDto(null,null,updatedCategoryId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{postId}", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json")
        )
                .andExpect(status().isOk())
                .andReturn();

        PostResponseDto p = mapper.readValue(result.getResponse().getContentAsString(),PostResponseDto.class);
        assertThat(p.getTitle()).isEqualTo(getTestData().getTitle());
        assertThat(p.getContent()).isEqualTo(getTestData().getContent());
        assertThat(p.getCategoryId()).isEqualTo(updatedCategoryId);
    }

    @Test
    public void deletePost_HttpStatusOk_SearchGivenPostId_ThenGetServiceException_ExceptionMessageNotFound() throws Exception {

        String postId = getTestData().getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{postId}", getTestData().getId()))
                .andExpect(res->assertThat(res.getResponse().getContentAsString())
                        .contains("deleted"));

        Exception ex = assertThrows(ServiceException.class,()->{
            postsService.getPost(postId);
        });
        testDataPost = null;
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).contains("not found");
    }

    @Test
    public void addComment_CheckCommentIdNotNull() throws Exception {
        String newComment =  "Easy boy. It is gonna be OK! Try again once.";
        CommentDto request = new CommentDto(newComment);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT+"/{postId}/comments", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        CommentResponseDto c = mapper.readValue(result.getResponse().getContentAsString(), CommentResponseDto.class);

        assertThat(c.getId()).isNotNull();
    }

    @Test
    public void updateComment_CheckCommentContentSetWRTRequest() throws Exception {
        CommentDto request = new CommentDto("I can not access my account.!");
        CommentResponseDto c = postsService.addComment(getTestData().getId(),request);

        String updatedComment =   "Easy boy. It is gonna be OK! Try again once.";
        request.setComment(updatedComment);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put(API_ENDPOINT+"/{postId}/comments/{commentId}"
                        ,getTestData().getId(),c.getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        CommentResponseDto updatedCommentEntity = mapper.readValue(result.getResponse().getContentAsString(),CommentResponseDto.class);

        assertThat(updatedCommentEntity.getComment()).isEqualTo(updatedComment);
    }

    @Test
    public void deleteComment_ThenExpectResponseBodyContainsDelete() throws Exception {
        CommentDto request = new CommentDto("I can not access my account.!");
        CommentResponseDto c = postsService.addComment(getTestData().getId(),request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{postId}/comments/{commentId}"
                        ,getTestData().getId(),c.getId())
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(b->assertThat(b.getResponse().getContentAsString()).contains("delete"))
                .andReturn();
    }

    @Test
    public void uploadAttachment_CheckAttachmentIdNotNull() throws Exception {
        AttachmentDto request = new AttachmentDto("451238","new-file-upload.pdf");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(API_ENDPOINT+"/{postId}/attachments", getTestData().getId())
                .content(mapper.writeValueAsString(request ))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        AttachmentResponseDto c = mapper.readValue(result.getResponse().getContentAsString(), AttachmentResponseDto.class);

        assertThat(c.getId()).isNotNull();
    }

    @Test
    public void deleteAttachment_ThenExpectResponseBodyContainsDelete() throws Exception {
        AttachmentDto request = new AttachmentDto("451238","new-file-upload.pdf");
        AttachmentResponseDto a = postsService.addAttachment(getTestData().getId(),request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .delete(API_ENDPOINT+"/{postId}/attachments/{attachmentId}"
                        ,getTestData().getId(),a.getId())
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(b->assertThat(b.getResponse().getContentAsString()).contains("delete"))
                .andReturn();
    }

    @Test
    public void getPostInCategory_HttpStatusOk_ExpectOneRecord() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(API_ENDPOINT)
        .param("categoryId",getTestData().getCategoryId()))
                .andReturn();

        PostIdListInCategoryResponse p = mapper.readValue(result.getResponse().getContentAsString(), PostIdListInCategoryResponse.class);
        assertThat(p).isNotNull();
        assertThat(p.getPostIdList()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(p.getPostIdList().get(0).getId()).isEqualTo(getTestData().getId());
    }
}
