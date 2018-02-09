package ru.spbau.lecturenotes.firebase;

import java.sql.Date;

/**
 * Structure that will be returned to UI layer as a result of the attachmentFile request.
 */
public class Attachment {
    protected AttachmentId attachmentId;
    protected AttachmentType type;
    protected Date creationTimestamp;
    protected String reference;

    public AttachmentId getAttachmentId() {
        return attachmentId;
    }

    public AttachmentType getType() {
        return type;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getReference() {
        return reference;
    }
}
