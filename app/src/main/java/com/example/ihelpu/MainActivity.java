package com.example.ihelpu;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if(user != null){
            goToHomeActivity();
            return;
        }

        //Określenie przedmiotów
        Button login_button = findViewById(R.id.loginButton);
        login_button.setOnClickListener(this);

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        Button login_With_Google = findViewById(R.id.loginWithGoogle);
        login_With_Google.setOnClickListener(this);

        Button continue_Without_Login = findViewById(R.id.continueWithoutLogin);
        continue_Without_Login.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
    }

    //Reakcja na przyciski
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                logInUser();
                break;
            case R.id.register:
                toRegister();
                break;
            case R.id.loginWithGoogle:
                googleSignIn();
                break;
            case R.id.continueWithoutLogin:
                goToHomeActivity();
                break;
            case R.id.forgotPassword:
                goToPasswordResetActivity();
                break;
        }
    }

    private void logInUser() {
        EditText edit_email = findViewById(R.id.email);
        EditText edit_password = findViewById(R.id.password);

        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.textInputLayout1);
                til.setError("Nie podano adresu e-mail!");
                til.requestFocus();
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.textInputLayout1);
                til.setError("Wproawdzono błędny adres e-mail!");
                til.requestFocus();
            }

            if (password.isEmpty()) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.textInputLayout2);
                til.setError("Nie podano hasła!");
                til.requestFocus();
            }

            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    assert user != null;
                    if(user.isEmailVerified()){
                        goToHomeActivity();
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Użytkowanik nie został zweryfikowany. Aby to zrobić przejdź do skrzynki e-mail.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Nie udało się zalogować.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,"Nie udało się zalogować.", Toast.LENGTH_LONG).show());
    }

    private void toRegister(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }

    private void goToPasswordResetActivity(){
        Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
        startActivity(intent);
    }

    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
}