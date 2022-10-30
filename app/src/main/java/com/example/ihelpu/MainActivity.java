package com.example.ihelpu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ihelpu.account.PasswordReset;
import com.example.ihelpu.account.RegisterActivity;
import com.example.ihelpu.tools.tabs.TabsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Logowanie
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

        //Sprawdź, czy użytkownik nie jest już zalogowany (lub zalogowany jako użytkownik anonymus)
        if(user != null){
            goToHomeActivity();
            return;
        }

        //Określenie wartości
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

    private void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My lang", lang);
        editor.apply();
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
                withoutlogin();
                break;
            case R.id.forgotPassword:
                goToPasswordResetActivity();
                break;
        }
    }

    //Obszar lgoowania
    private void logInUser() {
        EditText edit_email = findViewById(R.id.email);
        EditText edit_password = findViewById(R.id.password);

        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        //Sprawdzanie poprawności wartości
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                TextInputLayout til = findViewById(R.id.textInputLayout1);
                til.setError("Nie podano adresu e-mail!");
                til.requestFocus();
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                TextInputLayout til = findViewById(R.id.textInputLayout1);
                til.setError("Wproawdzono błędny adres e-mail!");
                til.requestFocus();
            }

            if (password.isEmpty()) {
                TextInputLayout til = findViewById(R.id.textInputLayout2);
                til.setError("Nie podano hasła!");
                til.requestFocus();
            }

            return;
        }

        //Zaloguj się z FireBase
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                if(user.isEmailVerified()){
                    goToHomeActivity();
                }
                else {
                    //Czy użytkownik zweryfikował adres e-mail?
                    user.sendEmailVerification();
                    toast("Użytkowanik nie został zweryfikowany. Aby to zrobić przejdź do skrzynki e-mail.");
                }
            }
            else{
                toast("Nie udało się zalogować.");
            }
        });
    }

    //Logowanie z kontem Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> toast("Nie udało się zalogować."));
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

    //Otwarcie okna logowania Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                toast("Nie udało się zalogować.");
            }
        }
    }

    void withoutlogin(){
        mAuth.signInAnonymously()
                .addOnSuccessListener(authResult -> goToHomeActivity())
                .addOnFailureListener(e -> toast("Nie udało się zalogować."));
    }

    void toast(String toastText){
        Toast.makeText(MainActivity.this,toastText, Toast.LENGTH_LONG).show();
    }
}