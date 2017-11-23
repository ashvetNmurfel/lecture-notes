package ru.spbau.lecturenotes.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class MainController {
    private HashMap<String, PdfFileStorage> storage = new HashMap<>();

    public static final MainController INSTANCE = new MainController();
    private MainController() {}

    public PdfFileStorage getRootDirectory() {
        PdfFileStorage file = PdfFileStorage.createFile("Filename", "nothing", "goodFILE");
        ArrayList<PdfFileStorage> list1 = new ArrayList<>();
        list1.add(file);
        PdfFileStorage dir = PdfFileStorage.createDirectory("nestedDir", "woof", list1);
        ArrayList<PdfFileStorage> list2 = new ArrayList<>();
        list2.add(file);
        list2.add(dir);

        storage.put(file.getName(), file);
        storage.put(dir.getName(), dir);
        return PdfFileStorage.createDirectory("mainDir", "hah", list2);
    }

    public PdfFileStorage getDir(String name) {
        return storage.get(name);
    }

}
