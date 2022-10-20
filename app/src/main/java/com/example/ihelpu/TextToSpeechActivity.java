package com.example.ihelpu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class TextToSpeechActivity extends AppCompatActivity {
    ImageButton text_to_speech_execute;
    EditText text_to_speech_text;
    TextToSpeech text_to_speech;
    TextView top_text;
    ImageView logo_button;

    Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);



        text_to_speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    text_to_speech.setLanguage(Locale.getDefault());
                }
            }
        });

        text_to_speech_execute = findViewById(R.id.textToSpeechExecute);
        text_to_speech_text = findViewById(R.id.textToSpeechText);
        text_to_speech_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = text_to_speech_text.getText().toString();
                if (text.matches("")){
                    text_to_speech_text.setError("Nie podano tekstu!");
                }
                else {
                    text_to_speech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }
}

