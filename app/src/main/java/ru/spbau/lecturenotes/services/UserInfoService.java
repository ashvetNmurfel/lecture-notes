package ru.spbau.lecturenotes.services;

import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.DatabaseInterface;

public class UserInfoService {
    protected DatabaseInterface db;

    public UserInfoService(DatabaseInterface db) {
        this.db = db;
    }

    public UserInfo getUserInfo() {
        return db.getUserInfo();
    }
}
