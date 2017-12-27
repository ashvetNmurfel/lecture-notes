package ru.spbau.lecturenotes.services.comments;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

public class CommentBuilder {
    private MutablePdfComments instance;

    public CommentBuilder(@NotNull String author,
                          @NotNull String content) {
        instance = new MutablePdfComments(author, content, null, null);
    }

    public boolean add(@NotNull CommentAttachment attachment) {
        instance.addAttachment(attachment);
        return true;
    }

    @NotNull
    public PdfComment toPdfComment() {
        return instance;
    }
}
