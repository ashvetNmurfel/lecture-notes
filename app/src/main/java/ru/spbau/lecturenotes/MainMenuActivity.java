package ru.spbau.lecturenotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.spbau.lecturenotes.controllers.MainMenuController;
import ru.spbau.lecturenotes.data.PdfFileStorage;

public class MainMenuActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public static final String KEY_NODE_ID = "nodeId";

    public static Intent createIntentForNode(Context context, String nodeId) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra(MainMenuActivity.KEY_NODE_ID, nodeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();

        Bundle extras =  getIntent().getExtras();
        if (extras == null || extras.getString(KEY_NODE_ID) == null) {
            sectionsList = MainMenuController.INSTANCE.getRootDirectory().substorages();
        } else {
            sectionsList = MainMenuController.INSTANCE.getDir(extras.getString(KEY_NODE_ID)).substorages();
        }
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
        ListView pdfList = (ListView) findViewById(R.id.pdf_list);
        pdfList.setAdapter(adapter);

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

    protected void startAuthorisation() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                Toast.makeText(getApplicationContext(), "Auth failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
