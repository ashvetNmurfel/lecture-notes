package ru.spbau.lecturenotes.storage.firebase;

import com.google.firebase.firestore.ListenerRegistration;

import ru.spbau.lecturenotes.storage.ListenerController;

public class FirebaseListenerController implements ListenerController {
    ListenerRegistration registration;

    FirebaseListenerController(ListenerRegistration registration) {
        this.registration = registration;
    }

    public void stopListener() {
        registration.remove();
    }
}
