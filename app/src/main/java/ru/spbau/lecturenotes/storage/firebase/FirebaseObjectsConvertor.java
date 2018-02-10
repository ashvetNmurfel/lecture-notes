package ru.spbau.lecturenotes.storage.firebase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.AttachmentType;
import ru.spbau.lecturenotes.storage.CommentId;
import ru.spbau.lecturenotes.storage.Discussion;
import ru.spbau.lecturenotes.storage.DiscussionId;
import ru.spbau.lecturenotes.storage.DiscussionStatus;
import ru.spbau.lecturenotes.storage.Document;

public class FirebaseObjectsConvertor {
    public static Document toDocument(@NotNull final FirebaseDocument fdoc,
                                      @NotNull final List<DiscussionId> discussions) {
        return new Document(fdoc.getId(),
                discussions,
                fdoc.getStorageReference(),
                fdoc.getUpdateTimestamp());
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
}
