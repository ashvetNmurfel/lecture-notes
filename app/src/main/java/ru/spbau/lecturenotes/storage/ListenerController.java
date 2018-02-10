package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ListenerRegistration;

public class ListenerController {
    ListenerRegistration registration;

    public ListenerController(ListenerRegistration registration) {
        this.registration = registration;
    }
}
