package ru.spbau.lecturenotes.controllers;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import ru.spbau.lecturenotes.services.MetadataSyncService;
import ru.spbau.lecturenotes.services.UserInfoService;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class MainMenuController {
    protected static MainMenuController INSTANCE;
    protected final UserInfoService userService;
    protected final MetadataSyncService metadataService;
    private static final String TAG = "MainMenuController";

    public MainMenuController getInstance() {
        return INSTANCE;
    }

    protected MainMenuController(DatabaseInterface db) {
        this(
                new UserInfoService(db),
                new MetadataSyncService(db)
        );
    }

    protected MainMenuController(UserInfoService uservice,
                                 MetadataSyncService metaservice) {
        userService = uservice;
        metadataService = metaservice;
    }

    public static boolean initialize(DatabaseInterface db) {
        INSTANCE = new MainMenuController(db);
        return true;
    }

    public static UserInfo getUserInfo() {
        return INSTANCE.userService.getUserInfo();
    }

    public static ListenerController applyWhenGroupListChanges(final Consumer<List<GroupId>> consumer) {
        return INSTANCE.metadataService.listenToGroupsList(new ResultListener<List<GroupId>>() {
            @Override
            public void onResult(List<GroupId> result) {
                Log.i(TAG, "List of groups was received by controller. Handling to function...");
                consumer.accept(result);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Failed to load groups list into controller. Error: ", error);
            }
        });
    }

    public static ListenerController applyWhenDocumentListChanges(final @NotNull GroupId group,
                                                                  final @NotNull Consumer<List<DocumentId>> consumer) {
        return INSTANCE.metadataService.listenToDocumentList(group, new ResultListener<List<DocumentId>>() {
            @Override
            public void onResult(List<DocumentId> result) {
                Log.i(TAG, "List of documents was received by controller. Handling to function...");
                consumer.accept(result);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Failed to load documents list into controller. Error: ", error);
            }
        });
    }
}
