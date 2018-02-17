package ru.spbau.lecturenotes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.controllers.MainMenuController;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.identifiers.DocumentId;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.uiElements.DocumentListAdapter;

public class DocumentListActivity extends AppCompatActivity {
    private static final String TAG = "DocumentListActivity";
    protected ProgressBar spinner;
    protected ListView documents_list;
    protected ListenerController documentsListener;
    protected GroupId shownGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.documents_list_spinner);
        documents_list = (ListView) findViewById(R.id.documents_list);
        shownGroup = (GroupId) getIntent().getSerializableExtra("GROUP");
    }

    @Override
    protected void onStart() {
        spinner.setVisibility(View.VISIBLE);
        setDocumentsListener();
        super.onStart();
    }

    private void setDocumentsListener() {
        if (documentsListener != null) {
            Log.w(TAG, "Trying to set listener while it's still active. Denied");
            return;
        }
        documentsListener = MainMenuController.applyWhenDocumentListChanges(
                shownGroup,
                new Consumer<List<DocumentId>>() {
                    @Override
                    public void accept(List<DocumentId> documentIds) {
                        spinner.setVisibility(View.GONE);
                        showDocuments(documentIds);
                    }
                });
    }

    private void showDocuments(List<DocumentId> documentIds) {
        DocumentListAdapter adapter = new DocumentListAdapter(
                this, documentIds,
                new Consumer<DocumentId>() {
            @Override
            public void accept(DocumentId documentId) {
                Toast.makeText(getApplicationContext(), documentId.getFilename(), Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(DocumentListActivity.this, DocumentListActivity.class);
                //intent.putExtra("GROUP", groupId);
/*                downloadingProgressBar.setVisibility(View.VISIBLE);
                ///TODO: disable sign out
                MainMenuController.onGetDocumentList(groupId, new ResultListener<List<DocumentId>>() {
                    @Override
                    public void onResult(List<DocumentId> documentIds) {
                        Intent intent = new Intent(MainMenuActivity.this, DocumentListActivity.class);
                        intent.putExtra("DOCUMENTS", (Serializable) documentIds);
                        downloadingProgressBar.setVisibility(View.GONE);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable error) {
                        downloadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Failed to load data. Please, try again", Toast.LENGTH_LONG).show();
                    }
                }); */
            }
        });
        documents_list.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        unsubscribeFromDocumentListChanges();
        clearList();
        super.onStop();
    }

    private void clearList() {
        showDocuments(new ArrayList<DocumentId>());
    }

    private void unsubscribeFromDocumentListChanges() {
        if (documentsListener == null) {
            Log.w(TAG, "Attempting to unsubscribe with an empty listener");
        } else {
            documentsListener.stopListener();
        }
        documentsListener = null;
    }

}
