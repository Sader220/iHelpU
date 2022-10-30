package com.example.ihelpu.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ihelpu.MainActivity;
import com.example.ihelpu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity implements View.OnClickListener {

    private EditText forgotEmailText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private TextView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        forgotEmailText = findViewById(R.id.email);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        goBack = findViewById(R.id.goBack);

        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goBack:
                goBackMain();
                break;
            case R.id.resetPasswordButton:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String email = forgotEmailText.getText().toString().trim();
        if (email.isEmpty()) {
            TextInputLayout til = findViewById(R.id.textInputLayout1);
            til.setError("Nie podano adresu e-mail!");
            til.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            TextInputLayout til = findViewById(R.id.textInputLayout1);
            til.setError("Wproawdzono błędny adres e-mail!");
            til.requestFocus();
            return;
        }

        //Wysłanie e-mail na podany adres z linkiem do resetowania hasła
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    toast("Wiadomość weryfikacyjna została wysłana.");
                }
                else{
                    toast("Coś poszło nie tak. Spróbuj ponownie później.");
                }
            }
        });
    }

    private void goBackMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    void toast(String toastText){
        Toast.makeText(PasswordReset.this,toastText, Toast.LENGTH_LONG).show();
    }
}