package com.example.ihelpu.tools.communication;

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

import com.example.ihelpu.R;
import com.example.ihelpu.tools.tabs.TabsActivity;

public class TextToSpeechActivity extends AppCompatActivity {
    ImageButton textToSpeechExecute;
    EditText textToSpeechText;
    TextToSpeech textToSpeech;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        textToSpeechExecute = findViewById(R.id.textToSpeechExecute);
        textToSpeechText = findViewById(R.id.textToSpeechText);
        textToSpeechExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textToSpeechText.getText().toString();
                if (text.matches("")){
                    textToSpeechText.setError("Nie podano tekstu!");
                }
                else {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }
}

