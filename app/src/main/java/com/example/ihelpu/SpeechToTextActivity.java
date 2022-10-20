package com.example.ihelpu;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity implements View.OnClickListener {
    TextView top_text, text_to_speech_text;
    ImageView top_button;
    ImageButton mVoiceBtn;
    Button speech_to_text_clean;
    private static final int RC_STT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        top_button = (ImageView) findViewById(R.id.topHomeButton);
        top_button.setOnClickListener(this);

        top_text = (TextView) findViewById(R.id.topText);
        top_text.setText("Rejestracja głosu");

        mVoiceBtn = findViewById(R.id.speechToTextButton);
        mVoiceBtn.setOnClickListener(this);

        speech_to_text_clean = findViewById(R.id.speechToTextClean);
        speech_to_text_clean.setOnClickListener(this);

        text_to_speech_text = findViewById(R.id.textToSpeechText);

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RC_STT == 100) {
            text_to_speech_text.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toString());
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Powiedz coś");

        try{
            startActivityForResult(intent, RC_STT);
        }
        catch (Exception e){
            Toast.makeText(SpeechToTextActivity.this,"Wystąpił błąd! Spróbuj ponownie później.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void clean(){
        text_to_speech_text.setText("");
        return;
    }
}