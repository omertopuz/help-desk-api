package org.helpdesk.posts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helpdesk.posts.model.document.*;
import org.helpdesk.posts.exception.ServiceException;
import org.helpdesk.posts.model.dto.AttachmentDto;
import org.helpdesk.posts.model.dto.CommentDto;
import org.helpdesk.posts.model.dto.NewPostDto;
import org.helpdesk.posts.model.dto.UpdatePostDto;
import org.helpdesk.posts.model.dto.response.AttachmentResponseDto;
import org.helpdesk.posts.model.dto.response.CommentResponseDto;
import org.helpdesk.posts.model.dto.response.PostResponseDto;
import org.helpdesk.posts.repository.PostRepo;
import org.helpdesk.posts.util.EnumPostStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    @Autowired
    private PostRepo postRepo;

//    @Autowired
//    private MessageChannel output;

    @Autowired
    private ObjectMapper modelMapper;

    public List<Posts> getAllPosts(){
        return postRepo.findAll();
    }

    public PostResponseDto createNewPost(NewPostDto request){
        Posts entity= modelMapper.convertValue(request, Posts.class);
        entity.setStateId(EnumPostStates.POST_CREATED.stateId());
        entity.setCurrentEvent(EnumPostStates.POST_CREATED);
        Optional.ofNullable(request.getAttachments())
                .ifPresent(c->{
                    entity.setAttachments(new ArrayList<>(Arrays.asList(modelMapper.convertValue(request.getAttachments(),Attachment[].class))));
                });

        postRepo.insert(entity);
        sendToQueue(entity.getId());
        return modelMapper.convertValue(entity, PostResponseDto.class);
    }

    private void sendToQueue(String postId) {
//        Message<String> message= MessageBuilder.withPayload(postId)
//                .setHeaderIfAbsent("contentType","application/json")
//                .build();
//        output.send(message);
    }

    public PostResponseDto getPost(String postId){
        return postRepo.findById(postId)
                .map(p->{
                    return modelMapper.convertValue(p,PostResponseDto.class);
                })
                .orElseThrow(()->new ServiceException("Post "+postId+" not found"));
    }

    public PostResponseDto updatePost(String postId, UpdatePostDto request){
        return postRepo.findById(postId)
                .map(post->{
                    if (post.getAssignedUsers()!=null && post.getAssignedUsers().size()>0)
                        throw new ServiceException(postId + " post has been already assigned for evaluation. It can not be revised.");

                    post.setStateId(EnumPostStates.POST_UPDATED.stateId());
                    post.setCurrentEvent(EnumPostStates.POST_UPDATED);
                    post.setTitle(Optional.ofNullable(request.getTitle()).orElse(post.getTitle()));
                    post.setContent(Optional.ofNullable(request.getContent()).orElse(post.getContent()));
                    post.setCategoryId(Optional.ofNullable(request.getCategoryId()).orElse(post.getCategoryId()));

                    return modelMapper.convertValue(postRepo.save(post),PostResponseDto.class);
                })
                .orElseThrow(()->new ServiceException("Post "+postId+" not found"));
    }

    public void deletePost(String postId){
        postRepo.findById(postId)
                .ifPresent(post->{
                    if (post.getAssignedUsers()!=null && post.getAssignedUsers().size()>0)
                        throw new ServiceException(postId + " post has been already assigned for evaluation. It can not be deleted.");
                    postRepo.delete(post);
                });

    }

    public CommentResponseDto addComment(String postId, CommentDto request){
        return postRepo.findById(postId)
                .map(post -> {
                    Comments comments = modelMapper.convertValue(request,Comments.class);
                    post.setCurrentEvent(EnumPostStates.COMMENT_CREATED);
                    comments.setStateId(EnumPostStates.COMMENT_CREATED.stateId());

                    post.setComments(Optional.ofNullable(post.getComments()).orElseGet(ArrayList::new));
                    post.getComments().add(comments);
                    postRepo.save(post);
                    return modelMapper.convertValue(comments,CommentResponseDto.class);
                })
                .orElseThrow(()->new ServiceException("Post "+postId+" not found"));
    }

    public CommentResponseDto updateComment(String postId, String commentId, CommentDto request){
        return postRepo.findById(postId)
                .map(post -> {
                    Comments comments = post.getComments()
                            .stream().filter(p->p.getId().equals(commentId)).findFirst()
                            .orElseThrow(()->new ServiceException("Comment "+commentId+" not found"));
                    // TODO check if the comment has been read or not

                    comments.setComment(request.getComment());

                    post.setCurrentEvent(EnumPostStates.COMMENT_UPDATED);
                    comments.setStateId(EnumPostStates.COMMENT_UPDATED.stateId());

                    postRepo.save(post);
                    return modelMapper.convertValue(comments,CommentResponseDto.class);
                })
                .orElseThrow(()->new ServiceException("Post "+postId+" not found"));

    }

    public void deleteComment(String postId,String commentId){
        postRepo.findById(postId)
                .ifPresent(post->{
                    Comments comments = post.getComments()
                            .stream().filter(p->p.getId()!=null && p.getId().equals(commentId)).findAny()
                            .orElseThrow(()->new ServiceException("Comment "+commentId+" not found"));
                    post.getComments().remove(comments);
                    post.setCurrentEvent(EnumPostStates.COMMENT_DELETED);
                    postRepo.save(post);
                });

    }

    public AttachmentResponseDto addAttachment(String postId, AttachmentDto request){
        return postRepo.findById(postId)
                .map(post -> {
                    Attachment attachment = modelMapper.convertValue(request,Attachment.class);
                    post.setCurrentEvent(EnumPostStates.ATTACHMENT_ADDED);
                    attachment.setStateId(EnumPostStates.ATTACHMENT_ADDED.stateId());

                    post.setAttachments(Optional.ofNullable(post.getAttachments()).orElseGet(ArrayList::new));
                    post.getAttachments().add(attachment);

                    postRepo.save(post);
                    return modelMapper.convertValue(attachment,AttachmentResponseDto.class);
                })
                .orElseThrow(()->new ServiceException("Post "+postId+" not found"));

    }

    public void deleteAttachment(String postId,String attachmentId){
        postRepo.findById(postId)
                .ifPresent(post->{
                    Attachment attachment = post.getAttachments()
                            .stream().filter(p->p.getId()!=null && p.getId().equals(attachmentId)).findAny()
                            .orElseThrow(()->new ServiceException("Attachment "+attachmentId+" not found"));
                    post.getAttachments().remove(attachment);
                    post.setCurrentEvent(EnumPostStates.ATTACHMENT_DELETED);
                    postRepo.save(post);
                });
    }

}
