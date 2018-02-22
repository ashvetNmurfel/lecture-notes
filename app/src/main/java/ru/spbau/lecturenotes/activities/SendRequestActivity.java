package ru.spbau.lecturenotes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.spbau.lecturenotes.R;
import ru.spbau.lecturenotes.controllers.MainMenuController;

public class SendRequestActivity extends AppCompatActivity {
    // UI elements
    protected Button sendRequestButton;
    protected EditText groupRequestEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        sendRequestButton = findViewById(R.id.send_group_request_button);
        groupRequestEditText = findViewById(R.id.group_request_edit);

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupRequestEditText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please, enter the name of the group",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String group = groupRequestEditText.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"conotica.social@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, group);
                i.putExtra(Intent.EXTRA_TEXT   , "Hi!\nMy name is "
                        + MainMenuController.getUserInfo().getName()
                        + "(" + MainMenuController.getUserInfo().getEmail()+ ")"
                        + " and I would like to join the group '"
                        + group + "'");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SendRequestActivity.this,
                            "There are no email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
