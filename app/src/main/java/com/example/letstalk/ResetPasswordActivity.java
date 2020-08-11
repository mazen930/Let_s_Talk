package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editText;
    Button reset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initialize();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    // Better function to ensure that this email has right pattern (i.e. example@gmail.com)
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void resetPassword() {
        if (isValidEmail(editText.getText().toString())) {
            firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        String error = Objects.requireNonNull(task.getException(), "some thing is wrong with Task exception").getMessage();
                        Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            editText.setError("Please enter a valid email address");
            editText.requestFocus();
        }

    }

    void initialize() {
        // initialize tool bar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar(), "No tool bar found").setTitle("Reset your Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// used to return back to parent activity which can be assigned in manifest

        editText = findViewById(R.id.forget_email);
        reset = findViewById(R.id.reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}