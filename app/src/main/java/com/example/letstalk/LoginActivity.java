package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialEditText email, password;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInWithEmailAndPassword(
                        Objects.requireNonNull(email.getText(), "Fields should not be empty").toString(),
                        Objects.requireNonNull(password.getText(), "Fields should not be empty ").toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else
                                    Toast.makeText(LoginActivity.this, "Email or password is incorrect ", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        // initialize fields
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.login_button);
        forgetPassword = findViewById(R.id.forget_password);

        // initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // initialize tool bar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar(), "No tool bar found").setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}