package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.File;
import java.io.IOException;


public class RandomPronunciationActivity extends AppCompatActivity implements NavigationMenu {
    private int RECORD_AUDIO_REQUEST_CODE =123 ;

    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawerLayout;
    public String pathSave = "";
    MediaRecorder mediaRecorder;
    boolean isRecording = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation);
        final TextView textStatus = findViewById(R.id.textStatus);
        ImageButton mic = findViewById(R.id.micButton);
        final TextView randomWord = findViewById(R.id.textSaySet);
        //TODO: Запрос рандомного слова
        mic.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getPermissionToRecordAudio();
                    }
                    if (checkPermissions()){

                        pathSave = getExternalCacheDir().getAbsolutePath();
                        pathSave = pathSave + File.separator + "RecordedSpeech.3gp"; //"/RecordedSpeech.3gp"
                        setUpMediaRecorder();
                        try{
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            isRecording = true;
                        }
                        catch(IOException e){

                        }
                        textStatus.setVisibility(View.VISIBLE);
                        //Toast.makeText(PronunciationActivity.this, "Recording..", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    if(isRecording){
                        mediaRecorder.stop();
                        textStatus.setVisibility(View.INVISIBLE);
                        //Toast.makeText(PronunciationActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
                        //TODO: Send the recording to the server and get the result
                        //TODO: Add the result to the history on server

                        Intent intent = new Intent(RandomPronunciationActivity.this, ResultActivity.class);
                        intent.putExtra("word", randomWord.getText().subSequence(4,randomWord.getText().length()));
                        startActivity(intent);
                    }
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    isRecording = false;
                }
                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void setUpMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickTrain(View view){
        recreate();
    }

    public void ClickChosenTrain(View view){redirectActivity(this, SearchActivity.class);}

    public void ClickHistory(View view){
        redirectActivity(this, History.class);
    }

    public void ClickAboutUs(View view){
        redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void redirectActivity(Activity activity, Class aclass) {
        Intent intent = new Intent(activity, aclass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }

    private boolean checkPermissions(){
        int writeStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int record = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return writeStorage == PackageManager.PERMISSION_GRANTED && readStorage == PackageManager.PERMISSION_GRANTED
                && record == PackageManager.PERMISSION_GRANTED;
    }
}
