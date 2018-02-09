package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;


/**
 * Structure that will be returned to UI layer as a result of the discussion request.
 */
class Discussion {
    protected DiscussionLocation location;
    protected DiscussionId discussionId;
    protected Comment rootComment;
    protected DiscussionStatus status;
    protected Date timestamp;

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

    @Nullable
    public Date getTimestamp() {
        return timestamp;
    }
}
