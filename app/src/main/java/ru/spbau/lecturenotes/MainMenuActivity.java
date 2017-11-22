package ru.spbau.lecturenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.spbau.lecturenotes.data.PdfFileStorage;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
        sectionsList.add(new PdfFileStorage(false, "Name", "info"));
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
        ListView pdfList = findViewById(R.id.pdf_list);
        pdfList.setAdapter(adapter);
        setContentView(R.layout.listview_pdf_item);
    }
}
