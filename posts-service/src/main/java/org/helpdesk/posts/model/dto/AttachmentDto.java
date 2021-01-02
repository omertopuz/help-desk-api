package org.helpdesk.posts.model.dto;

import org.helpdesk.posts.model.document.AbstractDocument;

public class AttachmentDto{
    private String fileId;
    private String fileName;

    public AttachmentDto() {
    }

    public AttachmentDto(String fileName) {
        this.fileName = fileName;
    }

    public AttachmentDto(String fileId, String fileName) {
        this.fileId = fileId;
        this.fileName = fileName;
    }

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
}
