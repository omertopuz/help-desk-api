package org.helpdesk.posts.model.dto.response;

import org.helpdesk.posts.util.EnumPostStates;

public class AttachmentResponseDto extends AbstractResponse {
    private String fileId;
    private String fileName;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
