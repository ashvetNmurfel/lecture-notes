package ru.spbau.lecturenotes.data;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Document {
    protected String id;
    protected String name;
    @ServerTimestamp
    protected Date timestamp;

    public String getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }
}
