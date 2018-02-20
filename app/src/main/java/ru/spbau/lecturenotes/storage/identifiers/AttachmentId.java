package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class AttachmentId implements Serializable {
    protected CommentId commentId;
    protected String key;

    public AttachmentId() {
    }

    public AttachmentId(final @NotNull CommentId commentId, final @NotNull String key) {
        this.commentId = commentId;
        this.key = key;
    }

    @NotNull
    public CommentId getCommentId() {
        return commentId;
    }

    @NotNull
    public DiscussionId discussionId() {
        return commentId.getDiscussionId();
    }

    @NotNull
    public GroupId groupId() {
        return commentId.groupId();
    }

    @NotNull
    public DocumentId documentId() {
        return commentId.documentId();
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
