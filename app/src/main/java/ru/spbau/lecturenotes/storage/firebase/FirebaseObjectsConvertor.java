package ru.spbau.lecturenotes.storage.firebase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.AttachmentType;
import ru.spbau.lecturenotes.storage.Comment;
import ru.spbau.lecturenotes.storage.CommentContent;
import ru.spbau.lecturenotes.storage.DocumentDataReference;
import ru.spbau.lecturenotes.storage.DocumentDataType;
import ru.spbau.lecturenotes.storage.identifiers.CommentId;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.DiscussionStatus;
import ru.spbau.lecturenotes.storage.Document;

public class FirebaseObjectsConvertor {
    public static Document toDocument(@NotNull final FirebaseDocument fdoc,
                                      @NotNull final List<DiscussionId> discussions) {
        return new Document(fdoc.getId(),
                discussions,
                toDocumentDataReference(fdoc.getReference()),
                fdoc.getUpdateTimestamp());
    }

    private static DocumentDataReference toDocumentDataReference(FirebaseDocumentDataReference reference) {
        return new DocumentDataReference(
                reference.numberOfPages,
                DocumentDataType.valueOf(reference.type),
                reference.storageReference);
    }

    public static Discussion toDiscussion(@NotNull final FirebaseDiscussion fdiscussion,
                                          @NotNull final List<CommentId> commentsList) {
        return new Discussion(
                fdiscussion.getId(),
                DiscussionStatus.valueOf(fdiscussion.getStatus()),
                fdiscussion.getTimestamp(),
                commentsList
        );
    }

    public static Attachment toAttachment(@NotNull final FirebaseAttachment fattachment) {
        return new Attachment(
                fattachment.getId(),
                AttachmentType.valueOf(fattachment.getType()),
                fattachment.getCreationTimestamp(),
                fattachment.getStorageReference()
        );
    }

    public static Comment toComment(FirebaseComment firebaseComment) {
        return new Comment(firebaseComment.getId(),
                firebaseComment.getParent(),
                FirebaseObjectsConvertor.toCommentContent(firebaseComment.getContent()),
                firebaseComment.getAuthor(),
                firebaseComment.getCreationTimestamp(),
                firebaseComment.isEdited(),
                firebaseComment.getEditTimestamp()
        );
    }

    private static CommentContent toCommentContent(FirebaseCommentContent content) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        for (int i = 0; i < content.getAttachments().size(); i++) {
            FirebaseAttachment firebaseAttachment = content.getAttachments().get(i);
            attachments.add(FirebaseObjectsConvertor.toAttachment(firebaseAttachment));
        }
        return new CommentContent(content.getText(), attachments);
    }
}
