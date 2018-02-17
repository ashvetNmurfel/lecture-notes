package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class FirebaseGroup {
    protected GroupId id;
    @ServerTimestamp
    protected Date creationTimestamp;

    public FirebaseGroup() {
    }

    public GroupId getId() {
        return id;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

}