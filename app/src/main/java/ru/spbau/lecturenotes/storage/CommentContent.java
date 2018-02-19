package ru.spbau.lecturenotes.storage;

import java.util.ArrayList;

public class CommentContent {
    protected String text;
    protected ArrayList<Attachment> attachments;

    public CommentContent(String text, ArrayList<Attachment> attachments) {
        this.text = text;
        this.attachments = attachments;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }
}
