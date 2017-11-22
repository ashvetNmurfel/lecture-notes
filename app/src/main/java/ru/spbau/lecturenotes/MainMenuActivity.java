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
        //ListView pdfList = findViewById()
        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
        sectionsList.add(new PdfFileStorage(false, "Name", "info"));
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);

        setContentView(R.layout.activity_main_menu);
    }
}
