package ru.spbau.lecturenotes.storage;

import org.jetbrains.annotations.NotNull;

public class CommentId {
    protected DiscussionId discussion;
    protected String key;

    public CommentId(@NotNull DiscussionId discussion, @NotNull String key) {
        this.discussion = discussion;
        this.key = key;
    }

    @NotNull
    public DiscussionId getDiscussion() {
        return discussion;
    }

    @NotNull
    public GroupId getGroupId() {
        return discussion.getGroupId();
    }

    @NotNull
    public DocumentId getDocumentId() {
        return discussion.getDocumentId();
    }

    @NotNull
    public String getKey() {
        return key;
    }
}
