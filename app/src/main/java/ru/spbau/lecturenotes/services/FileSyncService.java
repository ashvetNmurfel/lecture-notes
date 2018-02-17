package ru.spbau.lecturenotes.services;

import ru.spbau.lecturenotes.storage.DatabaseInterface;

public class FileSyncService {
    protected DatabaseInterface db;

    public FileSyncService(DatabaseInterface db) {
        this.db = db;
    }

}
