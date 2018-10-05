package com.mao.mp3_mdbsocials;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Views
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        //Buttons
        findViewById(R.id.create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    void updateUI(FirebaseUser user) {
        startActivity(new Intent(MainActivity.this, MainFeed.class));
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        FirebaseUser user = null;
        switch (b.getId()) {
            case R.id.create_account_button:
                user = FirebaseUtils.createAccount(MainActivity.this, mEmailField.getText().toString(), mPasswordField.getText().toString());
            case R.id.sign_in_button:
                user = FirebaseUtils.signIn(MainActivity.this, mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        if (user == null) {
            Toast.makeText(MainActivity.this, "Invalid input. Please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            updateUI(user);
        }
    }

}
