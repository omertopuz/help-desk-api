package org.helpdesk.posts.model.document;

import org.helpdesk.posts.util.EnumPostStates;

import java.util.Date;

public class PostHistory{

    private String explanation;
    private UserInfo userInfo;
    private Date createdDate;
    private EnumPostStates currentEvent;

    public PostHistory() {
    }

    public PostHistory(UserInfo user, String explanation) {
        super();
        this.explanation = explanation;
        this.userInfo = user;
    }

    public PostHistory(UserInfo userInfo, EnumPostStates currentEvent) {
        this.userInfo = userInfo;
        this.currentEvent = currentEvent;
        this.explanation = currentEvent.stateDescription();
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public EnumPostStates getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EnumPostStates currentEvent) {
        this.currentEvent = currentEvent;
    }
}
