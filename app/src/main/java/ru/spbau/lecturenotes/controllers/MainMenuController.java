package ru.spbau.lecturenotes.controllers;

import android.arch.core.util.Function;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

import ru.spbau.lecturenotes.data.PdfFileStorage;
import ru.spbau.lecturenotes.services.FileSyncService;
import ru.spbau.lecturenotes.services.MetadataSyncService;
import ru.spbau.lecturenotes.services.UserInfoService;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.Group;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.utils.DataHierarchyTree;

public class MainMenuController {
    private HashMap<String, PdfFileStorage> storage = new HashMap<>();

    protected static MainMenuController INSTANCE;
    protected final FileSyncService fileService;
    protected final UserInfoService userService;
    protected final MetadataSyncService metadataService;
    private static final String TAG = "MainMenuController";

    public MainMenuController getInstance() {
        return INSTANCE;
    }

    protected MainMenuController(DatabaseInterface db) {
        this(
                new FileSyncService(db),
                new UserInfoService(db),
                new MetadataSyncService(db)
        );
    }

    protected MainMenuController(FileSyncService fservice,
                                 UserInfoService uservice,
                                 MetadataSyncService metaservice) {
        fileService = fservice;
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

    public PdfFileStorage getRootDirectory() {
        ArrayList<PdfFileStorage> algosList = new ArrayList<>();
        ArrayList<PdfFileStorage> hwList = new ArrayList<>();
        ArrayList<PdfFileStorage> javaList = new ArrayList<>();
        ArrayList<PdfFileStorage> algebraList = new ArrayList<>();

        PdfFileStorage algos_conspect = PdfFileStorage.createFile("algorithms conspect", "lecture notes", new DocumentId(new GroupId(), "Somekey...", "Algos"));
        storage.put(algos_conspect.getName(), algos_conspect);
        PdfFileStorage algos_hw = PdfFileStorage.createFile("171117", "games", new DocumentId(new GroupId(), "Somekey...", "Games"));
        storage.put(algos_hw.getName(), algos_hw);
        hwList.add(algos_hw);
        PdfFileStorage hwDir = PdfFileStorage.createDirectory("homeworks", "", hwList);
        storage.put(hwDir.getName(), hwDir);
        algosList.add(hwDir);
        algosList.add(algos_conspect);


        PdfFileStorage algosDir = PdfFileStorage.createDirectory("Algorithms", "hws & lecture notes", algosList);
        storage.put(algosDir.getName(), algosDir);
        PdfFileStorage javaDir = PdfFileStorage.createDirectory("Java", "presentations", javaList);
        storage.put(javaDir.getName(), javaDir);
        PdfFileStorage algebraDir = PdfFileStorage.createDirectory("Algebra", "lecture notes & chep", algebraList);
        storage.put(algebraDir.getName(), algebraDir);
        PdfFileStorage history = PdfFileStorage.createFile("History", "Story of the world", new DocumentId(new GroupId(), "Somekey...", "History"));
        storage.put(history.getName(), history);


        ArrayList<PdfFileStorage> list = new ArrayList<>();
        list.add(javaDir);
        list.add(algosDir);
        list.add(algebraDir);
        list.add(history);
        return PdfFileStorage.createDirectory("root", "none info", list);
    }

    public PdfFileStorage getDir(String name) {
        return storage.get(name);
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
                Log.i(TAG, "List of groups was received by controller. Handling to function...");
                consumer.accept(result);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Failed to load groups list into controller. Error: ", error);
            }
        });
    }
}
