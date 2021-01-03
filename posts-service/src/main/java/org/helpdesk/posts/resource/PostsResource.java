package org.helpdesk.posts.resource;

import org.helpdesk.posts.model.dto.AttachmentDto;
import org.helpdesk.posts.model.dto.CommentDto;
import org.helpdesk.posts.model.dto.NewPostDto;
import org.helpdesk.posts.model.dto.UpdatePostDto;
import org.helpdesk.posts.model.dto.response.AttachmentResponseDto;
import org.helpdesk.posts.model.dto.response.CommentResponseDto;
import org.helpdesk.posts.model.dto.response.PostIdListInCategoryResponse;
import org.helpdesk.posts.model.dto.response.PostResponseDto;
import org.helpdesk.posts.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO check current auditor username should be same with post createdBy or current auditor has authorization except for createpost (everybody may create post)
@RestController
@RequestMapping("/api")
public class PostsResource {

    @Autowired
    private PostsService postsService;

//    @GetMapping(value = "/posts")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Posts> getAllPosts(){
//        return postsService.getAllPosts();
//    }

    @PostMapping(value = "/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponseDto> createPost(@RequestBody NewPostDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(postsService.createNewPost(request));
    }

    @GetMapping(value = "/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "postId") String postId){
        return ResponseEntity.ok().body(postsService.getPost(postId));
    }

    @PutMapping(value = "/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable(name = "postId") String postId,
                                                       @RequestBody UpdatePostDto request){
        return ResponseEntity.ok().body(postsService.updatePost(postId,request));
    }

    @DeleteMapping(value = "/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePost(@PathVariable(name = "postId") String postId){
        postsService.deletePost(postId);
        return ResponseEntity.ok().body("Post " + postId + " has been deleted");
    }

    @PostMapping(value = "/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable(name = "postId") String postId,@RequestBody CommentDto request){
        return ResponseEntity.ok().body(postsService.addComment(postId,request));
    }

    @GetMapping(value = "/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable(name = "postId") String postId){
        PostResponseDto result = postsService.getPost(postId);
        return ResponseEntity.ok().body(result.getComments());
    }

    @PutMapping(value = "/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable(name = "postId") String postId,
                                                       @PathVariable(name = "commentId") String commentId,
                                                       @RequestBody CommentDto request){
        return ResponseEntity.ok().body(postsService.updateComment(postId,commentId,request));
    }

    @DeleteMapping(value = "/posts/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") String postId,
                                                       @PathVariable(name = "commentId") String commentId){
        postsService.deleteComment(postId,commentId);
        return ResponseEntity.ok().body("comment deleted");
    }

    @PostMapping(value = "/posts/{postId}/attachments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AttachmentResponseDto> uploadAttachment(@PathVariable(name = "postId") String postId, @RequestBody AttachmentDto request){
        return ResponseEntity.ok().body(postsService.addAttachment(postId,request));
    }

    @GetMapping(value = "/posts/{postId}/attachments")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AttachmentResponseDto>> getAllAttachments(@PathVariable(name = "postId") String postId){
        return ResponseEntity.ok().body(postsService.getPost(postId).getAttachments());
    }

    @DeleteMapping(value = "/posts/{postId}/attachments/{attachmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteAttachments(@PathVariable(name = "postId") String postId,
                                                 @PathVariable(name = "attachmentId") String attachmentId){
        postsService.deleteAttachment(postId,attachmentId);
        return ResponseEntity.ok().body("comment deleted");
    }

    @GetMapping(value = "/posts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostIdListInCategoryResponse> getPostsInCategory(@RequestParam(name = "categoryId") String categoryId){
        return ResponseEntity.ok(postsService.getPostsInCategory(categoryId));
    }
//    @GetMapping(value = "/posts")
//    @ResponseStatus(HttpStatus.OK)
//    public PostIdListInCategoryResponse getPostsInCategory(@RequestParam(name = "categoryId") String categoryId){
//        return postsService.getPostsInCategory(categoryId);
//    }
}
