package ru.spbau.lecturenotes.firebase.requestStructures;

import ru.spbau.lecturenotes.firebase.uiData.CommentId;

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
