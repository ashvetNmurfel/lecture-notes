package ru.spbau.lecturenotes.storage;

import java.util.Date;
import java.util.List;

import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

/**
 * Structure that will be returned to UI layer as a result of the documentId request.
 */
public class Document {
    protected DocumentId id;
    protected List<DiscussionId> discussions;
    protected DocumentDataReference reference;
    protected Date updateTimestamp;

    public Document(DocumentId id, List<DiscussionId> discussions, DocumentDataReference  reference, Date updateTimestamp) {
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

    public DocumentDataReference getReference() {
        return reference;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }
}