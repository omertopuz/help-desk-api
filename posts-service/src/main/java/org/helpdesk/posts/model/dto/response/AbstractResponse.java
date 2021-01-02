package org.helpdesk.posts.model.dto.response;

import org.helpdesk.posts.util.EnumPostStates;

public class AbstractResponse {
    private String id;
    protected int stateId;
    protected EnumPostStates stateEnum;

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

}
