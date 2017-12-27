package ru.spbau.lecturenotes.data;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PdfComment {
    protected final String commentId;
    protected final boolean isEdited = false;
    protected final List<PdfComment> replies;
    protected final String author;
    protected final String content;
    Timestamp timestamp;
    // todo: Data: images, links...

    protected PdfComment(@NotNull final String author,
                         @NotNull final String content,
                         @NotNull final List<PdfComment> replies) {
        //todo: replace with a real id
        commentId = "";
        timestamp = new Timestamp(System.currentTimeMillis());
        this.author = author;
        this.content = content;
        this.replies = replies;
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
    public List<PdfComment> getThread() {
        return replies;
    }
}
