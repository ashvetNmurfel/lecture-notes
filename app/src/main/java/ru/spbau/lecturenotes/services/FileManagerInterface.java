package ru.spbau.lecturenotes.services;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.function.BiConsumer;

import ru.spbau.lecturenotes.storage.LocalFile;
import ru.spbau.lecturenotes.storage.ResultListener;

public interface FileManagerInterface<I, R> {
    void getFile(R referenceHolder,
                 ResultListener<LocalFile<I>> listener,
                 BiConsumer<File, ResultListener<LocalFile<I>>> downloader);

    class FileRemover extends AsyncTask<File, Void, Void> {
        final static String TAG = "FileRemover";
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Started removing files");
        }

        @Override
        protected Void doInBackground(File... files) {
            for (File file : files) {
                Log.i(TAG, "Removing file " + file.getName());
                if (file.delete()) {
                    Log.i(TAG, "Successfully removed " + file.getName());
                } else {
                    Log.w(TAG, "Failed to remove file" + file.getName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.i(TAG, "Finished removing files");
        }
    }
}
