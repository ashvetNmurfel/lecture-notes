package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class AttachmentId {
    protected CommentId commentId;
    protected String key;

    public AttachmentId(CommentId commentId, String key) {
        this.commentId = commentId;
        this.key = key;
    }

    public AttachmentId() {
    }

    @NotNull
    public CommentId getCommentId() {
        return commentId;
    }

    @NotNull
    public DiscussionId getDiscussion() {
        return commentId.getDiscussion();
    }

    @NotNull
    public GroupId getGroupId() {
        return commentId.getGroupId();
    }

    @NotNull
    public DocumentId getDocumentId() {
        return commentId.getDocumentId();
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
