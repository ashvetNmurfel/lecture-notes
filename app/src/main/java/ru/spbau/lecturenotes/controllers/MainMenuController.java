package ru.spbau.lecturenotes.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class MainMenuController {
    private HashMap<String, PdfFileStorage> storage = new HashMap<>();

    public static final MainMenuController INSTANCE = new MainMenuController();
    private MainMenuController() {}

    public PdfFileStorage getRootDirectory() {
        ArrayList<PdfFileStorage> algosList = new ArrayList<>();
        ArrayList<PdfFileStorage> hwList = new ArrayList<>();
        ArrayList<PdfFileStorage> javaList = new ArrayList<>();
        ArrayList<PdfFileStorage> algebraList = new ArrayList<>();

        PdfFileStorage algos_conspect = PdfFileStorage.createFile("algorithms conspect", "lecture notes", "algos_conspect.pdf");
        storage.put(algos_conspect.getName(), algos_conspect);
        PdfFileStorage algos_hw = PdfFileStorage.createFile("171117", "games", "algos_hw.pdf");
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
        PdfFileStorage history = PdfFileStorage.createFile("History", "Story of the world", "sample.pdf");
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

}
