package ru.spbau.lecturenotes.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

class Comment {
    protected CommentId id;
    protected CommentId parentId;
    protected CommentContent content;
    protected User author;
    @ServerTimestamp
    protected Date creationTimestamp;
    protected boolean isEdited;
    protected Date editTimestamp;

    @NotNull
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
