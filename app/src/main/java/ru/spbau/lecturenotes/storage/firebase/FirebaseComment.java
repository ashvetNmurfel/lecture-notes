package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.User;

public class FirebaseComment {
    protected CommentId id;
    protected CommentId parent;
    protected FirebaseCommentContent content;
    protected User author;
    @ServerTimestamp
    protected Date creationTimestamp;
    protected boolean edited;
    @ServerTimestamp
    protected Date editTimestamp;

    public FirebaseComment() {

    }

    @NotNull
    public CommentId getId() {
        return id;
    }

    @NotNull
    public CommentId getParent() {
        return parent;
    }

    @NotNull
    public FirebaseCommentContent getContent() {
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
        return edited;
    }

    @NotNull
    public Date getEditTimestamp() {
        return editTimestamp;
    }
}
