package ru.spbau.lecturenotes.firebase.uiData;

import java.sql.Date;

/**
 * Structure that will be returned to UI layer as a result of the attachmentFile request.
 */
public class Attachment {
    protected AttachmentId attachmentId;
    protected AttachmentType type;
    protected Date creationTimestamp;
    protected String reference;

    public Attachment(AttachmentId attachmentId, AttachmentType type, Date creationTimestamp, String reference) {
        this.attachmentId = attachmentId;
        this.type = type;
        this.creationTimestamp = creationTimestamp;
        this.reference = reference;
    }

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
