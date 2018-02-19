package ru.spbau.lecturenotes.services;

import java.util.List;
import java.util.function.Consumer;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class FileSyncService {
    protected DatabaseInterface db;

    public FileSyncService(DatabaseInterface db) {
        this.db = db;
    }

    protected void checkConsistency() {

    }

    public void cleanCache() {
        // Пойти и удалить все файлы, которые не помечены сохраняемыми
    }

    public void onAttachmentsDownloaded(List<AttachmentId> attachments, ResultListener<List<Attachment>> listener) {
        // Скачать аттачменты в кеш
    }

    public void addDocumentToCache() {
        // Пометить документ сохраняемым
        // Проверить, что он скачан
        // Если нет, скачать.
        // Пометить, что скачали.
    }

    public void onDocumentDownloaded(DocumentId documentId, ResultListener<Document> listener) {
        // Пометить документ "скачивается"
        // Поставить скачиваться
        // Пометить скачанным
    }
}
