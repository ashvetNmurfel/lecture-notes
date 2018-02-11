package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Date;

import ru.spbau.lecturenotes.storage.GroupId;

public class FirebaseGroup {
    protected GroupId groupId;
    @ServerTimestamp
    protected Date creationTimestamp;

    public FirebaseGroup() {
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }
}