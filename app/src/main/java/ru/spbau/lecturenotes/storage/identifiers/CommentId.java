package ru.spbau.lecturenotes.storage.identifiers;

import org.jetbrains.annotations.NotNull;

public class CommentId {
    protected DiscussionId discussionId;
    protected String key;

    public CommentId(final @NotNull DiscussionId discussionId,
                     final @NotNull String key) {
        this.discussionId = discussionId;
        this.key = key;
    }

    @NotNull
    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    @NotNull
    public GroupId getGroupId() {
        return discussionId.getGroupId();
    }

    @NotNull
    public DocumentId getDocumentId() {
        return discussionId.getDocumentId();
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
