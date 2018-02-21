package ru.spbau.lecturenotes.storage.requests;

import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class AddDocumentRequest {
    protected DocumentSketch sketch;
    protected GroupId group;

    public AddDocumentRequest(DocumentSketch sketch, GroupId group) {
        this.sketch = sketch;
        this.group = group;
    }

    public DocumentSketch getSketch() {
        return sketch;
    }

    public GroupId getGroup() {
        return group;
    }
}
