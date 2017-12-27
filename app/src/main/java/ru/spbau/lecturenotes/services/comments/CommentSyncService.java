package ru.spbau.lecturenotes.services.comments;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

public class CommentSyncService {
    @NotNull
    public PdfComment createNewComment(@NotNull String author,
                                       @NotNull String content,
                                       @Nullable List<CommentAttachment> attachments) {
        return new MutablePdfComments(author, content, null, attachments);
    }

    @NotNull
    public PdfComment addReply(@NotNull PdfComment oldComment,
                               @NotNull String authorOfNewComment,
                               @NotNull String contentOfNewComment,
                               @Nullable List<CommentAttachment> attachmentsOfNewComment) {
        return ((MutablePdfComments) oldComment).addReply(
                createNewComment(authorOfNewComment, contentOfNewComment, attachmentsOfNewComment)
        );
    }
}
