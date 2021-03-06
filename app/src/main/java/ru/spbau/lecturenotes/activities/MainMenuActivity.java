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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.controllers.MainMenuController;
import ru.spbau.lecturenotes.storage.ListenerController;
import ru.spbau.lecturenotes.storage.UserInfo;
import ru.spbau.lecturenotes.storage.firebase.FirebaseProxy;
import ru.spbau.lecturenotes.storage.identifiers.GroupId;
import ru.spbau.lecturenotes.uiElements.GroupListAdapter;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainMenuActivity";

    protected ListenerController groupsListener = null;

    // UI elements
    protected ProgressBar spinner;
    protected TextView name;
    protected TextView email;
    protected ListView groupsList;

    // Sign in action result code
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        startActivity(new Intent(this, PdfActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Select Group");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeader = navigationView.getHeaderView(0);

        // Initializing UI elements
        spinner = findViewById(R.id.groups_spinner);
        groupsList = findViewById(R.id.groups_list);
        name = navigationHeader.findViewById(R.id.user_name);
        email = navigationHeader.findViewById(R.id.user_email);

        // Todo: move to setup
        MainMenuController.initialize(FirebaseProxy.getInstance());
        if (MainMenuController.getUserInfo() == null) {
            startAuthorisation();
        } else {
            showUserInfo();
        }
    }


    @Override
    protected void onStart() {
        spinner.setVisibility(View.VISIBLE);
        if (MainMenuController.getUserInfo() != null) {
            setGroupListener();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        unsubscribeFromGroupListChanges();
        clearList();
        super.onStop();
    }

    protected void clearList() {
        showGroups(new ArrayList<GroupId>());
    }

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        unsubscribeFromGroupListChanges();
                        clearList();
                        //ToDo: invalidate cache
                        startAuthorisation();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                showUserInfo();
                spinner.setVisibility(View.VISIBLE);
                setGroupListener();
            } else {
                startAuthorisation();
            }
        }
    }

    private void setGroupListener() {
        if (groupsListener != null) {
            Log.w(TAG, "Trying to set listener while it's still active. Denied");
            return;
        }
        groupsListener = MainMenuController.applyWhenGroupListChanges(new Consumer<List<GroupId>>() {
            @Override
            public void accept(List<GroupId> groupIds) {
                spinner.setVisibility(View.GONE);
                showGroups(groupIds);
            }
        });
    }

    private void unsubscribeFromGroupListChanges() {
        if (groupsListener == null) {
            Log.w(TAG, "Attempting to unsubscribe with an empty listener");
        } else {
            groupsListener.stopListener();
        }
        groupsListener = null;
    }

    private void showUserInfo() {
        UserInfo currentUser = MainMenuController.getUserInfo();
        name.setText(currentUser.getName());
        email.setText(currentUser.getEmail());
    }


    protected void showGroups(List<GroupId> groups) {
        GroupListAdapter adapter =
                new GroupListAdapter(this, groups, new Consumer<GroupId>() {
            @Override
            public void accept(GroupId groupId) {
                Intent intent = new Intent(
                        MainMenuActivity.this,
                        DocumentListActivity.class);
                intent.putExtra(DocumentListActivity.INTENT_GROUP_EXTRA, groupId);
                startActivity(intent);
            }
        });
        groupsList.setAdapter(adapter);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_join_request) {
            Intent intent = new Intent(MainMenuActivity.this, SendRequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            signOut();
            Toast.makeText(getApplicationContext(),
                    "To use this application further, you should be signed in",
                    Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
