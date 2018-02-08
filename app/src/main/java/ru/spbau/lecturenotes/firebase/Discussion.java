package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.data.PdfComment;

class Discussion {
    protected DiscussionLocation location;
    protected DiscussionId discussionId;
    protected Comment rootComment;
    protected DiscussionStatus status;

    public Discussion(@NotNull final DiscussionLocation location,
                      @NotNull final DiscussionId discussionId,
                      @NotNull final Comment rootComment) {
        this.location = location;
        this.discussionId = discussionId;
        this.rootComment = rootComment;
        status = DiscussionStatus.UNKNOWN;
    }

    @NotNull
    public DiscussionLocation getLocation() {
        return location;
    }

    @NotNull
    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    @NotNull
    public Comment getRootComment() {
        return rootComment;
    }

    @NotNull
    public DiscussionStatus getStatus() {
        return status;
    }
}
