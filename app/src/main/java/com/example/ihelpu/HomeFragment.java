package com.example.ihelpu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener {
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    TextView no_account, top_text, text_to_speech_text, mouse1_Text, speech_to_text_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        ImageButton text_to_speech_button = (ImageButton) myView.findViewById(R.id.textToSpeechButton);
        text_to_speech_button.setOnClickListener(this);
        TextView text_to_speech_text = (TextView) myView.findViewById(R.id.textToSpeechText);
        text_to_speech_text.setOnClickListener(this);

        ImageButton mouse1_button = (ImageButton) myView.findViewById(R.id.mouse1Button);
        mouse1_button.setOnClickListener(this);
        TextView mouse1_Text = (TextView) myView.findViewById(R.id.mouse1Text);
        mouse1_Text.setOnClickListener(this);

        ImageButton sign_language_button = (ImageButton) myView.findViewById(R.id.signLanguageButton);
        sign_language_button.setOnClickListener(this);
        TextView sign_language_text = (TextView) myView.findViewById(R.id.signLanguageText);
        sign_language_text.setOnClickListener(this);

        ImageButton speech_to_text_button = (ImageButton) myView.findViewById(R.id.speechToTextButton);
        speech_to_text_button.setOnClickListener(this);
        TextView speech_to_text_text = (TextView) myView.findViewById(R.id.speechToTextText);
        speech_to_text_text.setOnClickListener(this);

        Button sign_out = (Button) myView.findViewById(R.id.signOutButton);
        sign_out.setOnClickListener(this);

        top_text = (TextView) myView.findViewById(R.id.topText);
        top_text.setText("Narzędzia");

        no_account = (TextView) myView.findViewById(R.id.noAccount);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        return myView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textToSpeechButton:
            case R.id.textToSpeechText:
                toTextToSpeech();
                break;
            case R.id.mouse1Text:
            case R.id.mouse1Button:
                toMouse1Activity();
                break;
            case R.id.speechToTextButton:
            case R.id.speechToTextText:
                toSpeechToTextActivity();
                break;
            case R.id.signOutButton:
                logOutUser();
                break;
            case R.id.signLanguageButton:
            case R.id.signLanguageText:
                toReadSignLanguage();
                break;

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed (new Runnable(){
            public void run(){
                if (mAuth.getCurrentUser() == null) {
                    no_account.setText("Użytkownik nie jest zalogowany!");
                }
                else{
                    no_account.setText("");
                }
            }
        }, 1000);
    }

    private void toMainActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void toSpeechToTextActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), SpeechToTextActivity.class);
        startActivity(intent);
    }

    private void toMouse1Activity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), Mouse1Activity.class);
        startActivity(intent);
    }

    private void toTextToSpeech(){
        Intent intent = new Intent(getActivity().getApplicationContext(), TextToSpeechActivity.class);
        startActivity(intent);
    }

    private void toReadSignLanguage(){
        Intent intent = new Intent(getActivity().getApplicationContext(), ReadSignLanguage.class);
        startActivity(intent);
    }

    private void logOutUser(){
        if (mAuth.getCurrentUser() == null) {
            toMainActivity();
            return;
        }
        else{
            FirebaseAuth.getInstance().signOut();
            toMainActivity();
            return;
        }
    }
}