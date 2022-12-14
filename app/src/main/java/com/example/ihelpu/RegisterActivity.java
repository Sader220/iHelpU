package com.example.ihelpu;

import static android.view.View.VISIBLE;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView go_back;
    private EditText edit_login_register, edit_email_register, edit_password_register;
    private Button register_button;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        go_back = findViewById(R.id.goBack);
        go_back.setOnClickListener(this);

        register_button = findViewById(R.id.registerButton);
        register_button.setOnClickListener(this);

        edit_login_register = findViewById(R.id.loginRegister);
        edit_email_register = findViewById(R.id.email);
        edit_password_register = findViewById(R.id.passwordRegister);

        progress_bar = findViewById(R.id.progressBar);

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
        TextInputLayout til0 = (TextInputLayout) findViewById(R.id.textInputLayout1);
        TextInputLayout til1 = (TextInputLayout) findViewById(R.id.textInputLayout2);
        TextInputLayout til2 = (TextInputLayout) findViewById(R.id.textInputLayout3);

        String email = edit_email_register.getText().toString().trim();
        String password = edit_password_register.getText().toString().trim();
        String login = edit_login_register.getText().toString().trim();

        if (email.isEmpty() || login.isEmpty() || password.isEmpty()) {
            if (login.isEmpty()) {
                til0.setError("Podanie loginu jest wymagane");
                edit_login_register.requestFocus();
            }   else{til0.setError(null);}

            if (email.isEmpty()) {
                til1.setError("Podanie e-mail jest wymagane!");
                edit_email_register.requestFocus();
            }   else{til1.setError(null);}

            if (password.isEmpty()) {
                til2.setError("Podanie has??a jest wymagane!");
                edit_password_register.requestFocus();
            }
            else if (password.length() < 6){
                til2.setError(null);
                til2.setError("Has??o jest za kr??tkie! (min. 6 znak??w)");
                edit_password_register.requestFocus();
            } else{til2.setError(null);}
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til1.setError("Wproawdzono b????dny adres e-mail!");
            til1.requestFocus();
            return;
        }

        progress_bar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    RegisterUsers user = new RegisterUsers(login, email);
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"U??ytkowanik zosta?? pomy??lnie zarejestrowany. Na twoj?? skrzynk?? e-mail zosta?? przes??ana wiadomo???? o weryfikacji.", Toast.LENGTH_LONG).show();
                                progress_bar.setVisibility(View.GONE);
                                logIn();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,"Wyst??pi?? b????d! Spr??buj ponownie p????niej.", Toast.LENGTH_LONG).show();
                                progress_bar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Wyst??pi?? b????d! Spr??buj ponownie p????niej.", Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            }
        });
        progress_bar.setVisibility(View.GONE);
    }

    private void goBackMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void logIn(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }
}