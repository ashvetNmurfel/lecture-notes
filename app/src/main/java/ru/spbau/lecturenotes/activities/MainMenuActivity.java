package ru.spbau.lecturenotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.spbau.lecturenotes.PdfListAdapter;
import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.controllers.MainMenuController;
import ru.spbau.lecturenotes.data.PdfFileStorage;
import ru.spbau.lecturenotes.services.ServicesInitializer;
import ru.spbau.lecturenotes.services.UserInfoService;
import ru.spbau.lecturenotes.storage.Group;
import ru.spbau.lecturenotes.storage.ResultListener;
import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RC_SIGN_IN = 123;
    public static final String KEY_NODE_ID = "nodeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Group");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ServicesInitializer.initializeAll();
        startAuthorisation();

    }

    protected void afterAuth () {
        FirebaseProxy.getInstance().getGroupsList(new ResultListener<List<GroupId>>() {
            @Override
            public void onResult(List<GroupId> result) {
                for (GroupId group : result) {
                    if (group == null)

                        Toast.makeText(getApplicationContext(), "Found group:" , Toast.LENGTH_LONG).show();
                }
                showGroups(result);
            }
            @Override
            public void onError(Throwable error) {
                FirebaseProxy.getInstance().getGroupsList(this);
            }
        });
    }

    protected void showGroups(List<GroupId> groups) {
        ArrayList<PdfFileStorage> sectionsList = new ArrayList<>();
        for (GroupId group : groups) {
            sectionsList.add(PdfFileStorage.createDirectory(group.getName(), "", new ArrayList<PdfFileStorage>()));
        }


        Bundle extras =  getIntent().getExtras();
        if (extras == null || extras.getString(KEY_NODE_ID) == null) {
            //sectionsList = MainMenuController.INSTANCE.getRootDirectory().substorages();
        } else {
            //sectionsList = MainMenuController.INSTANCE.getDir(extras.getString(KEY_NODE_ID)).substorages();
        }
        PdfListAdapter adapter = new PdfListAdapter(this, sectionsList);
        ListView pdfList = (ListView) findViewById(R.id.pdf_list);
        pdfList.setAdapter(adapter);
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

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startAuthorisation();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                UserInfo currentUser = UserInfoService.getUserInfo();

                TextView name = (TextView) findViewById(R.id.userName);
                TextView email = (TextView) findViewById(R.id.userEmail);
                name.setText(currentUser.getName());
                email.setText(currentUser.getEmail());
                afterAuth();
            } else {
                startAuthorisation();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Do you like settings?", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_out) {
            signOut();
            Toast.makeText(getApplicationContext(), "To use application, you should be signed in", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "Cool! I also love settings!", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
