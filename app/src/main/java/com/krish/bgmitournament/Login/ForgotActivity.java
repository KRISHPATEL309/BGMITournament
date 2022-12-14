package com.krish.bgmitournament.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.krish.bgmitournament.R;

public class ForgotActivity extends AppCompatActivity {

    TextInputLayout forgotEmail;
    Button forgotButton;
    TextView forgotLogin;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        forgotEmail = findViewById(R.id.forgotEmail);
        forgotButton = findViewById(R.id.forgotButton);
        forgotLogin = findViewById(R.id.forgotLogin);
        auth=FirebaseAuth.getInstance();

        forgotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        String email = forgotEmail.getEditText().getText().toString();

        if (email.isEmpty()){
            Toast.makeText(ForgotActivity.this, "Please Fill Email Id...", Toast.LENGTH_SHORT).show();
        }else {
            sendEmail();
        }

    }

    private void sendEmail() {
        auth.sendPasswordResetEmail(forgotEmail.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotActivity.this, "Email send please check your email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
                            finish();
                        }
                    }
                });
    }
}