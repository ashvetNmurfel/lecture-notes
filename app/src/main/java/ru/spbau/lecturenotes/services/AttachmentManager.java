package ru.spbau.lecturenotes.services;

import android.content.Context;
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

import ru.spbau.lecturenotes.storage.Attachment;
import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.AttachmentId;

public class AttachmentManager implements FileManagerInterface<AttachmentId, Attachment>, Serializable {
    private static final String TAG = "AttachmentManager";
    private static final String PATH =  "attachments_records";
    protected static AttachmentManager INSTANCE;
    protected Context context;
    private int version = 0;
    private int recordedAt = 0;

    private HashMap<AttachmentId, List<ResultListener<LocalFile<AttachmentId>>>> onAttachmentDownloaded;
    private HashMap<AttachmentId, File> descriptors;
    private HashSet<AttachmentId> downloadedFiles;
    private HashSet<AttachmentId> downloadingFiles;

    static AttachmentManager initInstance(Context context) {
        AttachmentManager am1 = null;
        AttachmentManager am2 = null;

        try {
            am1 = new SelfDownloader().execute(
                    context.getFilesDir().getAbsolutePath()
                            + File.separator
                            + PATH + "0").get();
        } catch (InterruptedException | ExecutionException e) {
            Log.wtf(TAG, "Couldn't get AttachmentManager#0", e);
        }
        try {
            am2 = new SelfDownloader().execute(
                    context.getFilesDir().getAbsolutePath()
                            + File.separator
                            + PATH + "1").get();
        } catch (InterruptedException | ExecutionException e) {
            Log.wtf(TAG, "Couldn't get AttachmentManager#1", e);
        }

        if (am1 == null && am2 == null) {
            INSTANCE = new AttachmentManager();
        } else if (am1 != null && am2 != null) {
            INSTANCE = am1.version > am2.version ? am1 : am2;
        } else {
            INSTANCE = am1 == null ? am2 : am1;
        }

        List<File> filesToBeRemoved = new ArrayList<>();
        for (AttachmentId attachment : INSTANCE.downloadingFiles) {
            filesToBeRemoved.add(INSTANCE.descriptors.get(attachment));
        }
        for (AttachmentId attachment : INSTANCE.downloadedFiles) {
            filesToBeRemoved.add(INSTANCE.descriptors.get(attachment));
        }
        INSTANCE.descriptors = new HashMap<>();
        INSTANCE.downloadingFiles = new HashSet<>();
        INSTANCE.downloadedFiles = new HashSet<>();
        INSTANCE.context = context;
        new FileRemover().execute(filesToBeRemoved.toArray(new File[filesToBeRemoved.size()]));
        INSTANCE.saveSelf();
        return INSTANCE;
    }

    AttachmentManager getInstance() {
        return INSTANCE;
    }


    protected AttachmentManager() {
        version = 0;
        downloadedFiles = new HashSet<>();
        downloadingFiles= new HashSet<>();
        descriptors = new HashMap<>();
        onAttachmentDownloaded = new HashMap<>();
    }

    @Override
    public void getFile(Attachment referenceHolder,
                        final ResultListener<LocalFile<AttachmentId>> listener,
                        BiConsumer<File, ResultListener<LocalFile<AttachmentId>>> downloader) {
        final AttachmentId id = referenceHolder.getAttachmentId();
        if (downloadedFiles.contains(id)) {
            listener.onResult(new LocalFile<>(id, descriptors.get(id)));
            return;
        }
        if (downloadingFiles.contains(id)) {
            attachCallback(id, listener);
            return;
        }
        // then file should be downloaded now
        File newDescriptor = generateAttachmentFile(id);
        descriptors.put(id, newDescriptor);
        onAttachmentDownloaded.put(id, Collections.singletonList(listener));
        downloader.accept(newDescriptor, new ResultListener<LocalFile<AttachmentId>>() {
            @Override
            public void onResult(LocalFile<AttachmentId> result) {
                INSTANCE.downloadingFiles.remove(id);
                INSTANCE.downloadedFiles.add(id);
                saveSelf();
                for (ResultListener<LocalFile<AttachmentId>> localFileResultListener :
                        onAttachmentDownloaded.get(id)) {
                    localFileResultListener.onResult(result);
                }
                onAttachmentDownloaded.remove(id);
            }

            @Override
            public void onError(Throwable error) {
                for (ResultListener<LocalFile<AttachmentId>> localFileResultListener :
                        onAttachmentDownloaded.get(id)) {
                    localFileResultListener.onError(error);
                }
                onAttachmentDownloaded.remove(id);
                INSTANCE.saveSelf();
            }
        });
    }

    private File getFile(String path) {
        File file = new File(INSTANCE.context.getFilesDir(), path);
        file.mkdirs();
        return file;
    }

    private File generateAttachmentFile(AttachmentId id) {
        return getFile(TextUtils.join(File.separator, Arrays.asList("attachments", UUID.randomUUID())));
    }

    private void attachCallback(AttachmentId id, ResultListener<LocalFile<AttachmentId>> listener) {
        onAttachmentDownloaded.get(id).add(listener);
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
            INSTANCE.recordedAt = 1 - INSTANCE.recordedAt;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = INSTANCE.getFile(AttachmentManager.PATH + Integer.toString(INSTANCE.recordedAt));
            ObjectOutputStream stream = null;
            try {
                stream = new ObjectOutputStream(new FileOutputStream(file));
                stream.writeObject(INSTANCE);
            } catch (IOException e) {
                Log.e(TAG, "Failed to write AttachmentManager on disk", e);
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
            if (needSync) {
                needSync = false;
                new SelfWriter().execute();
            }
        }
    }

    private static class SelfDownloader  extends AsyncTask<String, Void, AttachmentManager> {
        @Override
        protected AttachmentManager doInBackground(String... strings) {
            File file = new File(strings[0]);
            ObjectInputStream stream;
            AttachmentManager result = null;
            try {
                stream = new ObjectInputStream(new FileInputStream(file));
                try {
                    result = (AttachmentManager) stream.readObject();
                } catch (ClassNotFoundException e) {
                    Log.w(TAG, "Attachment manager file on path" + strings[0] + " was broken", e);
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed to read AttachmentManager record on path" + strings[0], e);
            }
            return result;
        }
    }
}
