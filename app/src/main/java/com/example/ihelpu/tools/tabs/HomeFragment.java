package com.example.ihelpu.tools.tabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ihelpu.MainActivity;
import com.example.ihelpu.R;
import com.example.ihelpu.tools.communication.ReadSignLanguage;
import com.example.ihelpu.tools.planner.PlannerActivity;
import com.example.ihelpu.tools.communication.SpeechToTextActivity;
import com.example.ihelpu.tools.communication.TextToSpeechActivity;
import com.example.ihelpu.tools.mouse.Mouse1Activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener {
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView noAccount, topText;
    ConstraintLayout mouse1Open, textToSpeechOpen, speechToTextOpen, signLanguageOpen, plannerOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("login");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mouse1Open = myView.findViewById(R.id.mouse1Open);
        mouse1Open.setOnClickListener(this);

        textToSpeechOpen = myView.findViewById(R.id.textToSpeechOpen);
        textToSpeechOpen.setOnClickListener(this);

        speechToTextOpen = myView.findViewById(R.id.speechToTextOpen);
        speechToTextOpen.setOnClickListener(this);

        signLanguageOpen = myView.findViewById(R.id.signLanguageOpen);
        signLanguageOpen.setOnClickListener(this);

        plannerOpen = myView.findViewById(R.id.plannerOpen);
        plannerOpen.setOnClickListener(this);

        Button sign_out = myView.findViewById(R.id.signOutButton);
        sign_out.setOnClickListener(this);

        topText = myView.findViewById(R.id.topText);
        topText.setText("Narzędzia");

        noAccount = myView.findViewById(R.id.noAccount);
        return myView;
    }

    //Reakcja na przyciski
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textToSpeechOpen:
                toTextToSpeech();
                break;
            case R.id.mouse1Open:
                toMouse1Activity();
                break;
            case R.id.speechToTextOpen:
                toSpeechToTextActivity();
                break;
            case R.id.signOutButton:
                logOutUser();
                break;
            case R.id.signLanguageOpen:
                toReadSignLanguage();
                break;
            case R.id.plannerOpen:
                toPlaner();
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Sprawdź czy użytkownik jest zlogowany
        new Handler().postDelayed (new Runnable(){
            @SuppressLint("SetTextI18n")
            public void run(){
                if (mAuth.getCurrentUser().isAnonymous()) {
                    noAccount.setText("Użytkownik nie jest zalogowany");
                } else{
                    noAccount.setText("");
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

    private void toPlaner(){
        Intent intent = new Intent(getActivity().getApplicationContext(), PlannerActivity.class);
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