package ru.spbau.lecturenotes.storage;

public class CommentContent {
    protected String text;
    protected Attachment[] attachments;

    public CommentContent(String text, Attachment[] attachments) {
        this.text = text;
        this.attachments = attachments;
    }

    public String getText() {
        return text;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }
}
