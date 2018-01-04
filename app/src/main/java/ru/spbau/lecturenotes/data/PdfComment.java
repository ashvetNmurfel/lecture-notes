package ru.spbau.lecturenotes.data;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

public class PdfComment {
    protected final String commentId;
    protected final boolean isEdited = false;
    protected final ArrayList<PdfComment> replies;
    protected final String author;
    protected String content;
    protected Timestamp timestamp;
    protected ArrayList<CommentAttachment> attachments;

    protected PdfComment(@NotNull final String author,
                         @NotNull final String content,
                         @NotNull final ArrayList<PdfComment> replies,
                         @NotNull final ArrayList<CommentAttachment> attachments) {
        //todo: replace with a real id
        commentId = "";
        timestamp = new Timestamp(System.currentTimeMillis());
        this.author = author;
        this.content = content;
        this.replies = replies;
        this.attachments = attachments;
    }

    public boolean isEdited() {
        return isEdited;
    }

    @NotNull
    public String getAuthorName() {
        return author;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    @NotNull
    public Date getTimestamp() {
        return timestamp;
    }

    @NotNull
    public String getId() {
        return commentId;
    }

    @NotNull
    public List<PdfComment> getThread() {
        return replies;
    }

    @NotNull
    public  List<CommentAttachment> getAttachments() {
        return attachments;
    }
}
