package ru.spbau.lecturenotes.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PdfFileStorage implements Serializable {
    private final boolean isDirectory;
    private final String name;
    private final String info;
    private final String file;
    private final ArrayList<PdfFileStorage> substorages;

    public static PdfFileStorage createFile(String name, String info, String file) {
        return new PdfFileStorage(false, name, info, file,  null);
    }

    public static PdfFileStorage createDirectory(String name, String info, ArrayList<PdfFileStorage> substorages) {
        return new PdfFileStorage(true, name, info, null, substorages);
    }

    private PdfFileStorage(boolean isDirectory, String name, String info, String file, ArrayList<PdfFileStorage> substorages) {
        this.isDirectory = isDirectory;
        this.substorages = substorages;
        this.file = file;
        this.info = info;
        this.name = name;
    }

    public ArrayList<PdfFileStorage> substorages() {
        return substorages;
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

    public String getFile() {
        return file;
    }

}
