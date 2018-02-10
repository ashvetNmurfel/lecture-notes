package ru.spbau.lecturenotes.firebase;

import java.io.File;

public class NewAttachmentRequest {
    protected CommentId commentId;
    protected AttachmentSketch attachment;

    public NewAttachmentRequest(CommentId commentId, AttachmentSketch attachment) {
        this.commentId = commentId;
        this.attachment = attachment;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public AttachmentSketch getAttachment() {
        return attachment;
    }
}
