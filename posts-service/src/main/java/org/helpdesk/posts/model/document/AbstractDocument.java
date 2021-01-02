package org.helpdesk.posts.model.document;

import org.helpdesk.posts.util.EnumPostStates;
import org.springframework.data.annotation.*;

import java.util.Date;

public abstract class AbstractDocument{

    @Id
    protected String id;

    @CreatedBy
    protected UserInfo createdBy;

    @LastModifiedBy
    protected UserInfo lastModifiedBy;

    @CreatedDate
    protected Date creationDate;

    @LastModifiedDate
    protected Date updateDate;

    protected int stateId;

    @Transient
    protected EnumPostStates currentEvent;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public UserInfo getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserInfo createdBy) {
        this.createdBy = createdBy;
    }

    public UserInfo getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(UserInfo lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public EnumPostStates getStateEnum() {
        return EnumPostStates.toEnumPostState(stateId);
    }

    public EnumPostStates getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EnumPostStates currentEvent) {
        this.currentEvent = currentEvent;
    }
}
