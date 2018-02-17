package ru.spbau.lecturenotes.services;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Group;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class MetadataSyncService {
    protected final DatabaseInterface db;

    public MetadataSyncService(DatabaseInterface db) {
        this.db = db;
    }

    public ListenerController listenToGroupsList(final @NotNull ResultListener<List<GroupId>> listener) {
        return db.setGroupsListListener(new DatabaseInterface.GroupListChangeListener() {
            @Override
            public void onGroupListUpdated() {
               db.getGroupsList(listener);
            }
        });
    }

    public ListenerController listenToDocumentList(final @NotNull GroupId group, final @NotNull ResultListener<List<DocumentId>> listener) {
        return db.setDocumentListListener(group, new DatabaseInterface.DocumentListChangeListener() {
            @Override
            public void onDocumentListUpdated(final @NotNull GroupId groupId) {
                db.getDocumentsList(groupId, listener);
            }
        });
    }
}
