package ru.spbau.lecturenotes.services.comments;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

public class CommentSyncService {
    public PdfComment createNewComment(@NotNull String author,
                                       @NotNull String content,
                                       List<CommentAttachment> attachments) {
        return new MutablePdfComments(author, content, null, attachments);
    }

    public PdfComment addReply(@NotNull PdfComment oldComment,
                               @NotNull String authorOfNewComment,
                               @NotNull String contentOfNewComment,
                               @NotNull List<CommentAttachment> attachmentsOfNewComment) {
        return ((MutablePdfComments) oldComment).addReply(
                createNewComment(authorOfNewComment, contentOfNewComment,attachmentsOfNewComment)
        );
    }
}
