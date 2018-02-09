package ru.spbau.lecturenotes.firebase;

import java.sql.Date;
import java.util.List;

/**
 * Structure that will be returned to UI layer as a result of the document request.
 */
public class Document {
    protected DocumentId id;
    protected String filename;
    protected List<DiscussionId> discussions;
    protected String path;
    protected Date updateTimestamp;

    public DocumentId getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public List<DiscussionId> getDiscussions() {
        return discussions;
    }

    public String getPath() {
        return path;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }
}