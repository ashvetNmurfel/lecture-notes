package ru.spbau.lecturenotes.services.comments;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

public class CommentBuilder {
    private MutablePdfComments instance;

    public CommentBuilder(@NotNull String author,
                          @NotNull String content) {
        instance = new MutablePdfComments(author, content, null, null);
    }

    public CommentBuilder(@NotNull String author,
                          @NotNull String content,
                          @NotNull CommentAttachment... attachments) {
        ArrayList<CommentAttachment> attachmentsList = new ArrayList<>();
        attachmentsList.addAll(Arrays.asList(attachments));
        instance = new MutablePdfComments(author, content, null, attachmentsList);
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
