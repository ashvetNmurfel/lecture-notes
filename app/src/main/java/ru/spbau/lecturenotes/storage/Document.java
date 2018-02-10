package ru.spbau.lecturenotes.storage;

import java.sql.Date;
import java.util.List;

/**
 * Structure that will be returned to UI layer as a result of the document request.
 */
public class Document {
    protected DocumentId id;
    protected List<DiscussionId> discussions;
    protected String reference;
    protected Date updateTimestamp;

    public Document(DocumentId id, List<DiscussionId> discussions, String reference, Date updateTimestamp) {
        this.id = id;
        this.discussions = discussions;
        this.reference = reference;
        this.updateTimestamp = updateTimestamp;
    }

    public DocumentId getId() {
        return id;
    }

    public List<DiscussionId> getDiscussions() {
        return discussions;
    }

    public String getReference() {
        return reference;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }
}