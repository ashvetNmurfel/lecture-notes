package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class CommentId implements Serializable {
    protected DiscussionId discussionId;
    protected String key;

    public CommentId(final @NotNull DiscussionId discussionId,
                     final @NotNull String key) {
        this.discussionId = discussionId;
        this.key = key;
    }

    public CommentId() {
    }

    @NotNull
    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    @NotNull
    public GroupId groupId() {
        return discussionId.groupId();
    }

    @NotNull
    public DocumentId documentId() {
        return discussionId.getDocumentId();
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
