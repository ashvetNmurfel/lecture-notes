package ru.spbau.lecturenotes.firebase;

import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

class CommentContent {
    protected String content;
    protected CommentAttachment[] attachments;

    public String getContent() {
        return content;
    }

    public CommentAttachment[] getAttachments() {
        return attachments;
    }
}
