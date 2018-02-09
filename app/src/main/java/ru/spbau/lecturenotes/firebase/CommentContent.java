package ru.spbau.lecturenotes.firebase;

public class CommentContent {
    protected String text;
    protected AttachmentId[] attachmentsIds;

    public String getText() {
        return text;
    }

    public AttachmentId[] getAttachmentsIds() {
        return attachmentsIds;
    }
}
