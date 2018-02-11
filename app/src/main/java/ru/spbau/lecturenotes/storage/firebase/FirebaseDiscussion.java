package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;

public class FirebaseDiscussion {
    protected DiscussionId id;
    protected String status;
    @ServerTimestamp
    protected Date timestamp;

    public FirebaseDiscussion() {
    }

    public DiscussionId getId() {
        return id;
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    @NotNull
    public Date getTimestamp() {
        return timestamp;
    }
}
