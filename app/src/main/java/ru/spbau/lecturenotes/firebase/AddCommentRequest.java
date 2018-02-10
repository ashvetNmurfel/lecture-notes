package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

public class AddCommentRequest {
    protected DiscussionId discussionId;
    protected CommentSketch comment;

    public AddCommentRequest(@NotNull final DiscussionId discussionId,
                             @NotNull final CommentSketch comment) {
        this.discussionId = discussionId;
        this.comment = comment;
    }

    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    public CommentSketch getComment() {
        return comment;
    }
}
