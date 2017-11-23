package ru.spbau.lecturenotes.data;

import java.io.File;
import java.util.List;

public class PdfFileStorage {
    private final boolean isDirectory;
    private final String name;
    private final String info;
    private final File file;
    private final List<PdfFileStorage> substorages;

    public static PdfFileStorage createFile(String name, String info, File file) {
        return new PdfFileStorage(false, name, info, file,  null);
    }

    public static PdfFileStorage createDirectory(String name, String info, List<PdfFileStorage> substorages) {
        return new PdfFileStorage(true, name, info, null, substorages);
    }

    private PdfFileStorage(boolean isDirectory, String name, String info, File file, List<PdfFileStorage> substorages) {
        this.isDirectory = isDirectory;
        this.substorages = substorages;
        this.file = file;
        this.info = info;
        this.name = name;
    }

    public List<PdfFileStorage> substorages() {
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

    public File getFile() {
        return file;
    }

}
