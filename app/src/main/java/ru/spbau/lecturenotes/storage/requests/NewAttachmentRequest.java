package ru.spbau.lecturenotes.storage.requests;

import ru.spbau.lecturenotes.storage.CommentId;

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
