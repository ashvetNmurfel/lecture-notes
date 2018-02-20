package ru.spbau.lecturenotes.services;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;

public class DocumentsManager implements FileManagerInterface<DocumentId, Document>, Serializable {
    private static final String TAG = "DocumentsManager";
    private static final String PATH =  "documents_records";
    protected static final DocumentsManager INSTANCE;
    private int version = 0;
    private int recordedAt = 0;

    private HashMap<DocumentId, List<ResultListener<LocalFile<DocumentId>>>> onDocumentDownloaded;
    private HashMap<DocumentId, File> descriptors;
    private HashSet<DocumentId> downloadedFiles;
    private HashSet<DocumentId> downloadingFiles;

    static {
        DocumentsManager dm1 = null;
        DocumentsManager dm2 = null;

        try {
            dm1 = new SelfDownloader().execute(PATH + "0").get();
        } catch (InterruptedException | ExecutionException e) {
            Log.wtf(TAG, "Couldn't get DownloadManager#0", e);
        }
        try {
            dm2 = new SelfDownloader().execute(PATH + "1").get();
        } catch (InterruptedException | ExecutionException e) {
            Log.wtf(TAG, "Couldn't get DownloadManager#1", e);
        }

        if (dm1 == null && dm2 == null) {
            INSTANCE = new DocumentsManager();
        } else if (dm1 != null && dm2 != null) {
            INSTANCE = dm1.version > dm2.version ? dm1 : dm2;
        } else {
            INSTANCE = dm1 == null ? dm2 : dm1;
        }

        List<File> filesToBeRemoved = new ArrayList<>();
        for (DocumentId document : INSTANCE.downloadingFiles) {
            filesToBeRemoved.add(INSTANCE.descriptors.get(document));
            INSTANCE.descriptors.remove(document);
        }
        INSTANCE.downloadingFiles = new HashSet<>();

        new FileRemover().execute(filesToBeRemoved.toArray(new File[filesToBeRemoved.size()]));
        INSTANCE.saveSelf();
    }

    DocumentsManager getInstance() {
        return INSTANCE;
    }


    protected DocumentsManager() {
        version = 0;
        downloadedFiles = new HashSet<>();
        downloadingFiles= new HashSet<>();
        descriptors = new HashMap<>();
        onDocumentDownloaded = new HashMap<>();
    }

    @Override
    public void getFile(Document referenceHolder,
                        final ResultListener<LocalFile<DocumentId>> listener,
                        BiConsumer<File, ResultListener<LocalFile<DocumentId>>> downloader) {
        final DocumentId id = referenceHolder.getId();
        if (downloadedFiles.contains(id)) {
            listener.onResult(new LocalFile<>(id, descriptors.get(id)));
            return;
        }
        if (downloadingFiles.contains(id)) {
            attachCallback(id, listener);
            return;
        }
        // then file should be downloaded now
        File newDescriptor = generateDocumentFile(id);
        descriptors.put(id, newDescriptor);
        onDocumentDownloaded.put(id, Collections.singletonList(listener));
        downloader.accept(newDescriptor, new ResultListener<LocalFile<DocumentId>>() {
            @Override
            public void onResult(LocalFile<DocumentId> result) {
                INSTANCE.downloadingFiles.remove(id);
                INSTANCE.downloadedFiles.add(id);
                saveSelf();
                for (ResultListener<LocalFile<DocumentId>> localFileResultListener :
                        onDocumentDownloaded.get(id)) {
                    localFileResultListener.onResult(result);
                }
                onDocumentDownloaded.remove(id);
                INSTANCE.saveSelf();
            }

            @Override
            public void onError(Throwable error) {
                for (ResultListener<LocalFile<DocumentId>> localFileResultListener :
                        onDocumentDownloaded.get(id)) {
                    localFileResultListener.onError(error);
                }
            }
        });
    }

    private File generateDocumentFile(DocumentId id) {
        return new File(TextUtils.join(File.separator, Arrays.asList("documents", UUID.randomUUID())));
    }

    private void attachCallback(DocumentId id, ResultListener<LocalFile<DocumentId>> listener) {
        onDocumentDownloaded.get(id).add(listener);
        saveSelf();
    }

    private void saveSelf() {
        version++;
        if (SelfWriter.saveInProgress) {
            SelfWriter.needSync = true;
        } else {
            new SelfWriter().execute();
        }
    }

    private static class SelfWriter extends AsyncTask<Void, Void, Void> {
        private static boolean saveInProgress;
        private static boolean needSync;
        @Override
        protected void onPreExecute() {
            saveInProgress = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(DocumentsManager.PATH + Integer.toString(INSTANCE.recordedAt));
            ObjectOutputStream stream = null;
            try {
                stream = new ObjectOutputStream(new FileOutputStream(file));
                stream.writeObject(INSTANCE);
            } catch (IOException e) {
                Log.e(TAG, "Failed to write DownloadManager on disk", e);
            } finally {
                try {
                    if (stream != null)
                        stream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close stream", e);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            saveInProgress = false;
            INSTANCE.recordedAt = 1 - INSTANCE.recordedAt;
            if (needSync) {
                needSync = false;
                new SelfWriter().execute();
            }
        }
    }

    private static class SelfDownloader  extends AsyncTask<String, Void, DocumentsManager> {
        @Override
        protected DocumentsManager doInBackground(String... strings) {
            File file = new File(strings[0]);
            ObjectInputStream stream;
            DocumentsManager result = null;
            try {
                stream = new ObjectInputStream(new FileInputStream(file));
                try {
                    result = (DocumentsManager) stream.readObject();
                } catch (ClassNotFoundException e) {
                    Log.w(TAG, "Document manager file on path" + strings[0] + " was broken", e);
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed to read DocumentsManager record on path" + strings[0], e);
            }
            return result;
        }
    }
}
