package ru.spbau.lecturenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
        sectionsList.add(PdfFileStorage.createFile("Name", "info", null));
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
        ListView pdfList = findViewById(R.id.pdf_list);
        pdfList.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "loaded", Toast.LENGTH_LONG).show();
    }
}
