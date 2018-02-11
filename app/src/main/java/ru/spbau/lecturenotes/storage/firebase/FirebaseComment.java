package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.User;

public class FirebaseComment {
    protected CommentId id;
    protected String parent;
    protected FirebaseCommentContent content;
    protected User author;
    @ServerTimestamp
    protected Date creationTimestamp;
    protected boolean isEdited;
    @ServerTimestamp
    protected Date editTimestamp;

    public FirebaseComment() {

    }

    @NotNull
    public CommentId getId() {
        return id;
    }

    @NotNull
    public String getParent() {
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
        return isEdited;
    }

    @NotNull
    public Date getEditTimestamp() {
        return editTimestamp;
    }
}
