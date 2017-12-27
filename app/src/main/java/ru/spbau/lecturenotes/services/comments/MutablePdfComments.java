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


     MutablePdfComments addReply(PdfComment newReply) {
        replies.add(newReply);
        return this;
    }
}
