package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

class AddCommentRequest {
    protected DiscussionId discussionId;
    protected Comment comment;

    public AddCommentRequest(@NotNull final DiscussionId discussionId,
                             @NotNull final Comment comment) {
        this.discussionId = discussionId;
        this.comment = comment;
    }

    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    public Comment getComment() {
        return comment;
    }
}
