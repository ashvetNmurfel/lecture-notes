package ru.spbau.lecturenotes.firebase;

public class AttachmentId {
    protected CommentId commentId;
    protected String key;

    public AttachmentId() {
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public String getKey() {
        return key;
    }
}
