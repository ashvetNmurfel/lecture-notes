package ru.spbau.lecturenotes.services;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.BiConsumer;

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.DatabaseInterface;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

/**
 * WARNING: Call from UI thread only
 */
public class FileSyncService {
    protected DatabaseInterface db;
    protected FileManagerInterface<AttachmentId, Attachment> attachmentManger;
    protected FileManagerInterface<DocumentId, Document> documentManager;

    public FileSyncService(@NotNull DatabaseInterface db,
                           @NotNull FileManagerInterface<AttachmentId, Attachment> attachmentManger,
                           @NotNull FileManagerInterface<DocumentId, Document> documentManger) {
        this.db = db;
        this.attachmentManger = attachmentManger;
        this.documentManager = documentManger;
    }

    public void onAttachmentDownloaded(@NotNull final Attachment attachment,
                                       @NotNull final ResultListener<LocalFile<AttachmentId>> listener) {
        attachmentManger.getFile(
                attachment,
                listener,
                new BiConsumer<File, ResultListener<LocalFile<AttachmentId>>>() {
            @Override
            public void accept(File file, ResultListener<LocalFile<AttachmentId>> listener) {
                db.getAttachmentContent(attachment, file, listener);
            }
        });
    }

    public void onDocumentDownloaded(@NotNull final Document document,
                                     @NotNull final ResultListener<LocalFile<DocumentId>> listener) {
        documentManager.getFile(document, listener,
                new BiConsumer<File, ResultListener<LocalFile<DocumentId>>>() {
            @Override
            public void accept(@NotNull File file,
                               @NotNull ResultListener<LocalFile<DocumentId>> localFileResultListener) {
                db.getDocumentFile(document, file, listener);
            }
        });
    }
}
