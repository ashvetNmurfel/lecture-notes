package ru.spbau.lecturenotes.firebase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.spbau.lecturenotes.firebase.uiData.Attachment;
import ru.spbau.lecturenotes.firebase.uiData.AttachmentType;
import ru.spbau.lecturenotes.firebase.uiData.CommentId;
import ru.spbau.lecturenotes.firebase.uiData.Discussion;
import ru.spbau.lecturenotes.firebase.uiData.DiscussionId;
import ru.spbau.lecturenotes.firebase.uiData.DiscussionStatus;
import ru.spbau.lecturenotes.firebase.uiData.Document;

public class FirebaseObjectsConvertor {
    public static Document toDocument(@NotNull final FirebaseProxy.FirebaseDocument fdoc,
                                      @NotNull final List<DiscussionId> discussions) {
        return new Document(fdoc.getId(),
                discussions,
                fdoc.getStorageReference(),
                fdoc.getUpdateTimestamp());
    }

    public static Discussion toDiscussion(@NotNull final FirebaseProxy.FirebaseDiscussion fdiscussion,
                                          @NotNull final List<CommentId> commentsList) {
        return new Discussion(
                fdiscussion.getId(),
                DiscussionStatus.valueOf(fdiscussion.getStatus()),
                fdiscussion.getTimestamp(),
                commentsList
        );
    }

    public static Attachment toAttachment(@NotNull final FirebaseProxy.FirebaseAttachment fattachment) {
        return new Attachment(
                fattachment.getId(),
                AttachmentType.valueOf(fattachment.getType()),
                fattachment.getCreationTimestamp(),
                fattachment.getStorageReference()
        );
    }
}
