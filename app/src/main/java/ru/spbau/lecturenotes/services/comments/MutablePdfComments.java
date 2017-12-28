package ru.spbau.lecturenotes.services.comments;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.Utilities;
import ru.spbau.lecturenotes.data.PdfComment;
import ru.spbau.lecturenotes.data.attachments.CommentAttachment;

class MutablePdfComments extends PdfComment{
    MutablePdfComments(@NotNull String author,
                       @NotNull String content,
                       List<PdfComment> replies,
                       List<CommentAttachment> attachments) {
        super(author, content,
                replies != null ? Utilities.convertListToArrayList(replies) :
                    new ArrayList<PdfComment>(),
                attachments != null ? Utilities.convertListToArrayList(attachments) :
                    new ArrayList<CommentAttachment>()
        );
    }

    @NotNull
    MutablePdfComments addReply(@NotNull PdfComment newReply) {
        replies.add(newReply);
        return this;
    }

    @NotNull
    MutablePdfComments addContent(@NotNull String content) {
        this.content += content;
        return this;
    }

    @NotNull
    MutablePdfComments addAttachment(@NotNull CommentAttachment newAttachment) {
        attachments.add(newAttachment);
        return this;
    }
}
