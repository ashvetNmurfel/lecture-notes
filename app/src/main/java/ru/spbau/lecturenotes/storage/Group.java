package ru.spbau.lecturenotes.storage;

import java.sql.Date;

public class Group {
    protected GroupId id;
    protected Date timestamp;

    public Group() {
    }

    public Group(GroupId id, Date timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public GroupId getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
