package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.List;


/**
 * Structure that will be returned to UI layer as a result of the discussion request.
 */
public class Discussion {
    protected DiscussionLocation location;
    protected DiscussionId discussionId;
    protected DiscussionStatus status;
    protected Date timestamp;
    protected List<Comment> comments;

    @NotNull
    public DiscussionLocation getLocation() {
        return location;
    }

    @NotNull
    public DiscussionId getDiscussionId() {
        return discussionId;
    }

    @NotNull
    public List<Comment> getComments() {
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
