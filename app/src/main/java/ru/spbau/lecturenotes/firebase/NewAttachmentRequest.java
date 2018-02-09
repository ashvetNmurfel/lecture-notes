package ru.spbau.lecturenotes.firebase;

import java.io.File;

public class NewAttachmentRequest {
    protected CommentId commentId;
    protected File attachmentFile;
    protected AttachmentType type;

    public NewAttachmentRequest() {
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public File getAttachmentFile() {
        return attachmentFile;
    }

    public AttachmentType getType() {
        return type;
    }
}
