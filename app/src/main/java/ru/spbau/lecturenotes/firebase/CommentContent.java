package ru.spbau.lecturenotes.firebase;

public class CommentContent {
    protected String text;
    protected Attachment[] attachments;

    public String getText() {
        return text;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }
}
