package ru.spbau.lecturenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.lecturenotes.data.PdfFileStorage;


public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
        sectionsList.add(PdfFileStorage.createFile("Name", "info", null));
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
        ListView pdfList = (ListView) findViewById(R.id.pdf_list);
        pdfList.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "loaded", Toast.LENGTH_LONG).show();

//        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
//        sectionsList.add(new PdfFileStorage(false, "Name", "info"));
//        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
//        ListView pdfList = findViewById(R.id.pdf_list);
//        pdfList.setAdapter(adapter);
//        Toast.makeText(getApplicationContext(), "woho", Toast.LENGTH_LONG).show();
//        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "wtf, where am i", Toast.LENGTH_LONG).show();
//            }
//        });
//        setContentView(R.layout.listview_pdf_item);

//        Intent intent = null;
//        try {
//            intent = new Intent(this,
//                    Class.forName("com.github.barteksc.sample.PDFViewActivity"));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


//        Intent intent = new Intent();
//        intent.setClassName("name.murfel.fakemodule", "MainActivity");
//        startActivity(intent);


//        Intent intent = new Intent(this, SecondAct.class);
//        startActivity(intent);
    }
}
