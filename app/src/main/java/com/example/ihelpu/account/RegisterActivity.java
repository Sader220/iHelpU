package com.example.ihelpu.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ihelpu.MainActivity;
import com.example.ihelpu.R;
import com.example.ihelpu.RegisterUsers;
import com.example.ihelpu.tools.tabs.TabsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText editLoginRegister, editEmailRegister, editPasswordRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        TextView goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(this);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        editLoginRegister = findViewById(R.id.loginRegister);
        editEmailRegister = findViewById(R.id.email);
        editPasswordRegister = findViewById(R.id.passwordRegister);

        progressBar = findViewById(R.id.progressBar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

    }
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.goBack:
                goBackMain();
                break;
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        TextInputLayout til0 = findViewById(R.id.textInputLayout1);
        TextInputLayout til1 = findViewById(R.id.textInputLayout2);
        TextInputLayout til2 = findViewById(R.id.textInputLayout3);

        String email = editEmailRegister.getText().toString().trim();
        String password = editPasswordRegister.getText().toString().trim();
        String login = editLoginRegister.getText().toString().trim();

        if (email.isEmpty() || login.isEmpty() || password.isEmpty()) {
            if (login.isEmpty()) {
                til0.setError("Podanie loginu jest wymagane");
                editLoginRegister.requestFocus();
            } else {
                til0.setError(null);
            }

            if (email.isEmpty()) {
                til1.setError("Podanie e-mail jest wymagane!");
                editEmailRegister.requestFocus();
            } else {
                til1.setError(null);
            }

            if (password.isEmpty()) {
                til2.setError("Podanie hasła jest wymagane!");
                editPasswordRegister.requestFocus();
            } else if (password.length() < 6) {
                til2.setError(null);
                til2.setError("Hasło jest za krótkie! (min. 6 znaków)");
                editPasswordRegister.requestFocus();
            } else {
                til2.setError(null);
            }
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til1.setError("Wproawdzono błędny adres e-mail!");
            til1.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            RegisterUsers user = new RegisterUsers(login, email);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Użytkowanik został pomyślnie zarejestrowany. Na twoją skrzynkę e-mail został przesłana wiadomość o weryfikacji.", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                logIn();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Wystąpił błąd! Spróbuj ponownie później.", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Wystąpił błąd! Spróbuj ponownie później.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }

    private void goBackMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void logIn(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
        finish();
    }
}