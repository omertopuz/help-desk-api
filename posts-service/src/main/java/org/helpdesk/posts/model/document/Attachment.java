package org.helpdesk.posts.model.document;

public class Attachment extends AbstractDocument {
    private String fileName;
    private String fileId;

    public Attachment() {
    }

    public Attachment(String fileName, String fileId) {
        this.fileName = fileName;
        this.fileId = fileId;
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
