package ru.spbau.lecturenotes.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

import ru.spbau.lecturenotes.storage.identifiers.CommentId;


/**
 * Structure that will be returned to UI layer as a result of the comment request.
 */
public class Comment {
    protected CommentId id;
    protected CommentId parentId;
    protected CommentContent content;
    protected User author;
    protected Date creationTimestamp;
    protected boolean isEdited;
    protected Date editTimestamp;


    @Nullable
    public CommentId getId() {
        return id;
    }

    @Nullable
    public CommentId getParentId() {
        return parentId;
    }

    @NotNull
    public CommentContent getContent() {
        return content;
    }

    @NotNull
    public User getAuthor() {
        return author;
    }

    @NotNull
    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public boolean isEdited() {
        return isEdited;
    }

    @Nullable
    public Date getEditTimestamp() {
        return editTimestamp;
    }
}
