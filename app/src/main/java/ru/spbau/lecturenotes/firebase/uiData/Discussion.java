package ru.spbau.lecturenotes.firebase.uiData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.List;


/**
 * Structure that will be returned to UI layer as a result of the discussion request.
 */
public class Discussion {
    protected DiscussionId discussionId;
    protected DiscussionStatus status;
    protected Date timestamp;
    protected List<CommentId> comments;

    public Discussion(DiscussionId discussionId, DiscussionStatus status, Date timestamp, List<CommentId> comments) {
        this.discussionId = discussionId;
        this.status = status;
        this.timestamp = timestamp;
        this.comments = comments;
    }

    @NotNull
    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    @NotNull
    public List<CommentId> getComments() {
        return comments;
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
