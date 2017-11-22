package ru.spbau.lecturenotes.data;

import java.util.ArrayList;

public class PdfFileStorage {
    private final boolean isDirectory;
    private final String name;
    private final String info;

    private final ArrayList<PdfFileStorage> substorages = new ArrayList<>();

    public PdfFileStorage(boolean isDirectory, String name, String info) {
        this.isDirectory = isDirectory;
        this.info = info;
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return isDirectory;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

}
