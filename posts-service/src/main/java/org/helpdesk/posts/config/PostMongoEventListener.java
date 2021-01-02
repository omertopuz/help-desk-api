package org.helpdesk.posts.config;

import org.bson.types.ObjectId;
import org.helpdesk.posts.exception.ServiceException;
import org.helpdesk.posts.exception.UnAuthorizationException;
import org.helpdesk.posts.model.document.PostHistory;
import org.helpdesk.posts.model.document.Posts;
import org.helpdesk.posts.model.document.UserInfo;
import org.helpdesk.posts.util.EnumPostStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Configuration
public class PostMongoEventListener extends AbstractMongoEventListener<Posts> {

    @Autowired
    private AuditorAware<UserInfo> auditorAware;

    @Override
    public void onBeforeSave(BeforeSaveEvent<Posts> event) {
        //TODO check current auditor username same with post createdBy or current auditor has authorization
        if (!(event.getSource().getCreatedBy().getUserName()
                .equals(auditorAware.getCurrentAuditor().get()
                .getUserName())
        || hasAuthorization())){
            throw new UnAuthorizationException("Permission required. Not authorized.");
        }
    }

    private boolean hasAuthorization() {
        return true;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Posts> event) {
        Posts p = event.getSource();
        UserInfo auditor = auditorAware.getCurrentAuditor().orElse(null);
        p.setHistoryList(Optional.ofNullable(p.getHistoryList()).orElseGet(ArrayList::new));

        switch (p.getCurrentEvent()){
            case COMMENT_CREATED:
                Optional.ofNullable(p.getComments()).ifPresent(commentsList -> {
                    commentsList.stream().
                            filter(c->c.getStateId()== EnumPostStates.COMMENT_CREATED.stateId())
                            .forEach(n->{
                                n.setId(new ObjectId().toString());
                                n.setCreatedBy(auditor);
                                n.setCreationDate(new Date());
                            });
                });
                break;
            case COMMENT_UPDATED:
                Optional.ofNullable(p.getComments()).ifPresent(commentsList -> {
                    commentsList.stream().
                            filter(c->c.getStateId()== EnumPostStates.COMMENT_CREATED.stateId())
                            .forEach(n->{
                                n.setLastModifiedBy(auditor);
                                n.setUpdateDate(new Date());
                            });
                });
                break;
            case ATTACHMENT_ADDED:
                Optional.ofNullable(p.getAttachments()).ifPresent(commentsList -> {
                    commentsList.stream().
                            filter(c->c.getStateId()== EnumPostStates.ATTACHMENT_ADDED.stateId())
                            .forEach(n->{
                                n.setId(new ObjectId().toString());
                                n.setCreatedBy(auditor);
                                n.setCreationDate(new Date());
                            });
                });
                break;
            case POST_CREATED:
                Optional.ofNullable(p.getAttachments()).ifPresent(commentsList -> {
                    commentsList.stream()
                            .forEach(n->{
                                n.setId(new ObjectId().toString());
                                n.setCreatedBy(auditor);
                                n.setCreationDate(new Date());
                                n.setStateId(EnumPostStates.ATTACHMENT_ADDED.stateId());
                            });
                });
                break;
        }

        p.getHistoryList().add(new PostHistory(auditor, p.getCurrentEvent()));
    }

}
