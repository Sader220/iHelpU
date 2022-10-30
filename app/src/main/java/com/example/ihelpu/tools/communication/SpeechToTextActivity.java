package com.example.ihelpu.tools.communication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ihelpu.R;
import com.example.ihelpu.tools.tabs.TabsActivity;

import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity implements View.OnClickListener {
    TextView topText, textToSpeechText;
    ImageView topButton;
    ImageButton mVoiceBtn;
    Button speechToTextClean;

    private static final int RC_STT = 100;

    //Kod dotyczący funkcji nagrywania dźwięku, oraz zezwolenia na nagrywanie dźwięku
    private static final int RC_MPS = 101;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        //Zezwolenie na nagrywanie dźwięku
        if(checkMicrophone()){
            microphonePermissions();
        }

        topButton = findViewById(R.id.topHomeButton);
        topButton.setOnClickListener(this);

        topText = findViewById(R.id.topText);
        topText.setText("Rejestracja głosu");

        mVoiceBtn = findViewById(R.id.speechToTextButton);
        mVoiceBtn.setOnClickListener(this);

        speechToTextClean = findViewById(R.id.speechToTextClean);
        speechToTextClean.setOnClickListener(this);

        textToSpeechText = findViewById(R.id.textToSpeechText);

    }

    //Reakcja na przyciski
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topHomeButton:
                goToHomeActivity();
                break;
            case R.id.speechToTextButton:
                speak();
                break;
            case R.id.speechToTextClean:
                clean();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Jeżeli ustalono kod na poprawny, wezwij nagrywanie dźwięku
        if (RC_STT == 100) {
            assert data != null;
            textToSpeechText.setText(textToSpeechText.getText().toString() + " " + data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //Ustalenie języka systemowego jako głos
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Powiedz coś");

        try{
            startActivityForResult(intent, RC_STT);
        }
        catch (Exception e){
            toast("Wystąpił błąd! Spróbuj ponownie później.");
        }
    }

    private void clean(){
        textToSpeechText.setText("");
    }

    //Sprawdzenie czy urządzenie posiada mikrofon
    private boolean checkMicrophone(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            toast("Wystąpił błąd! Urządzenie nie posiada sprawnego mikrofonu.");
            return false;
        }
    }

    //Zezwolenie na nagrywanie dźwięku
    private void microphonePermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RC_MPS);
        } else{
            toast("Użytkownik nie przyznał wymaganych uprawńień!");
        }
    }

    void toast(String toastText){
        Toast.makeText(SpeechToTextActivity.this,toastText, Toast.LENGTH_LONG).show();
    }

}