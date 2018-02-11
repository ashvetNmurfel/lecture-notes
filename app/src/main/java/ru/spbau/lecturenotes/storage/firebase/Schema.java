package ru.spbau.lecturenotes.storage.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;
import ru.spbau.lecturenotes.storage.identifiers.DiscussionId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class Schema {
  public static final String GROUPS = "groups";
  public static final String DOCS = "documents";
  public static final String DISCUSSIONS = "discussions";
  public static final String COMMENTS = "comments";
  public static final String ATTACHMENTS = "attachments";

  @NonNull
  public static CollectionReference groups(FirebaseFirestore db) {
    return db
        .collection(GROUPS);
  }

  @NonNull
  public static CollectionReference documents(FirebaseFirestore db, @NotNull GroupId group) {
    return groups(db)
        .document(group.getKey())
        .collection(DOCS);
  }

  @NonNull
  public static DocumentReference document(FirebaseFirestore db, @NotNull DocumentId document) {
      return documents(db, document.getGroupId())
              .document(document.getKey());
  }

  @NonNull
  public static CollectionReference discussions(FirebaseFirestore db, @NotNull DocumentId document) {
    return document(db, document)
        .collection(DISCUSSIONS);
  }

  @NonNull
  public static DocumentReference discussion(FirebaseFirestore db, @NotNull DiscussionId discussion) {
      return discussions(db, discussion.getDocumentId())
              .document(discussion.getKey());
  }

  @NonNull
  public static CollectionReference attachments(FirebaseFirestore db, @NotNull DiscussionId discussion) {
    return discussion(db, discussion)
        .collection(ATTACHMENTS);
  }

  @NonNull
  public static CollectionReference comments(FirebaseFirestore db, @NotNull DiscussionId discussionId) {
    return discussion(db, discussionId)
        .collection(COMMENTS);
  }

  @NonNull
  public static DocumentReference attachment(FirebaseFirestore db, @NotNull AttachmentId attachment) {
      return attachments(db, attachment.getDiscussion())
              .document(attachment.getKey());
  }
}
