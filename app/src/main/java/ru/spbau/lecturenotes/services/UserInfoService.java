package ru.spbau.lecturenotes.services;

import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.DatabaseInterface;

public class UserInfoService {
    protected static UserInfoService INSTANCE;
    protected DatabaseInterface db;

    protected UserInfoService(DatabaseInterface db) {
        this.db = db;
    }

    public static UserInfoService getInstance() {
        return INSTANCE;
    }

    public static UserInfo getUserInfo() {
        return INSTANCE.db.getUserInfo();
    }

    public static boolean initialize(DatabaseInterface database) {
        INSTANCE = new UserInfoService(database);
        return true;
    }
}
