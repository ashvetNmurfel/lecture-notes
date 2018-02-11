package ru.spbau.lecturenotes.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.List;

import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;


/**
 * Structure that will be returned to UI layer as a result of the discussionId request.
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
