package ru.spbau.lecturenotes.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.controllers.MainMenuController;
import ru.spbau.lecturenotes.storage.Document;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.storage.requests.DocumentSketch;

public class AddDocumentActivity extends AppCompatActivity {
    private static final int RC_GET_PDF = 13;
    public static final String INTENT_GROUP_EXTRA = "INTENT_GROUP_EXTRA";

    // UI parts
    protected Button selectDocumentButton;
    protected Button saveDocumentButton;
    protected ImageView documentSelectedCheck;
    protected EditText documentNameEdit;
    protected EditText documentDescriptionEdit;
    protected ProgressBar uploadingDocumentSpinner;

    // Inserted data
    protected Uri selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);

        selectDocumentButton = findViewById(R.id.select_document_button);
        saveDocumentButton = findViewById(R.id.save_document_button);
        documentSelectedCheck = findViewById(R.id.document_selected_check);
        documentNameEdit = findViewById(R.id.document_name_edit);
        documentDescriptionEdit = findViewById(R.id.document_description_edit);
        uploadingDocumentSpinner = findViewById(R.id.uploading_document_spinner);

        final GroupId shownGroup = (GroupId) getIntent().getSerializableExtra(INTENT_GROUP_EXTRA);

        selectDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getFileIntent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(getFileIntent,
                        "Select file"),
                        RC_GET_PDF);
            }
        });
        saveDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFile == null) {
                    Toast.makeText(getApplicationContext(), "Please, select file", Toast.LENGTH_LONG).show();
                    return;
                }
                if (documentNameEdit.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please, enter document name", Toast.LENGTH_LONG).show();
                    return;
                }
                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(selectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                DocumentSketch sketch = new DocumentSketch(
                        stream,
                        documentNameEdit.getText().toString(),
                        documentDescriptionEdit.getText().toString());
                uploadingDocumentSpinner.setVisibility(View.VISIBLE);
                MainMenuController.addDocument(shownGroup, sketch, new ResultListener<Document>() {
                    @Override
                    public void onResult(Document result) {
                        Toast.makeText(getApplicationContext(), "Successfully loaded", Toast.LENGTH_LONG).show();
                        uploadingDocumentSpinner.setVisibility(View.GONE);
                        finish();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getApplicationContext(),
                                "Failed to upload document. Check your internet connection and try again",
                                Toast.LENGTH_LONG).show();
                        uploadingDocumentSpinner.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GET_PDF && resultCode == RESULT_OK) {
            selectedFile = data.getData();
            documentSelectedCheck.setVisibility(View.VISIBLE);
        }
    }
}

