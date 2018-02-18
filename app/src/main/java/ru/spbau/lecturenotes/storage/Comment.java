package ru.spbau.lecturenotes.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

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

    public Comment(CommentId id,
                   CommentId parentId,
                   CommentContent content,
                   User author,
                   Date creationTimestamp,
                   boolean isEdited,
                   Date editTimestamp) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.author = author;
        this.creationTimestamp = creationTimestamp;
        this.isEdited = isEdited;
        this.editTimestamp = editTimestamp;
    }

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
