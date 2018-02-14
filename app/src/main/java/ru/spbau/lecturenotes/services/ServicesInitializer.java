package ru.spbau.lecturenotes.services;

import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;

public class ServicesInitializer {
    public static boolean initializeAll() {
        DatabaseInterface database = FirebaseProxy.getInstance();
        return UserInfoService.initialize(database);
    }
}
