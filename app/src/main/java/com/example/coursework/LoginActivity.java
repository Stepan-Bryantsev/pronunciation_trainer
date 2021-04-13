package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "AccountData";
    public static final String APP_PREFERENCES_EMAIL = "Email";
    public static final String APP_PREFERENCES_PASSWORD = "Password";
    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final EditText login = findViewById(R.id.loginField);
        final EditText password = findViewById(R.id.passwordField);
        final TextView error = findViewById(R.id.errorText);
        TextView noAccount = findViewById(R.id.textNoAccount);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Request login from server
                //IF Login succeeded:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_EMAIL, login.getText().toString());
                editor.putString(APP_PREFERENCES_PASSWORD, password.getText().toString());
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, RandomPronunciationActivity.class);
                startActivity(intent);
                //else
                error.setVisibility(View.VISIBLE);
            }
        });
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mSettings.contains(APP_PREFERENCES_EMAIL) && mSettings.contains(APP_PREFERENCES_PASSWORD)) {
            String email = mSettings.getString(APP_PREFERENCES_EMAIL, "");
            String password = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
//            if (email != "Email" && password != "Password"){
//                //Try login through server:
//                Intent intent = new Intent(LoginActivity.this, RandomPronunciationActivity.class);
//                startActivity(intent);
//            }
        }
    }
}
