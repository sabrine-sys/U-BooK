package com.fsb.e_commerceapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fsb.e_commerceapplication.MainActivity;
import com.fsb.e_commerceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    EditText email,password ;
    Button signIn;
    TextView signUp ;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        this.email = findViewById(R.id.email_login);
        progressBar = findViewById(R.id.progressbar_login);
        progressBar.setVisibility(View.GONE);
        this.password = findViewById(R.id.password_login);
        this.signIn = findViewById(R.id.login_btn);
        this.signUp = findViewById(R.id.sign_up_login);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,RegistrationActivity.class));
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loginUser();
            }
        });
    }

    private void loginUser() {
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();

        if(TextUtils.isEmpty(userMail)){
            Toast.makeText(this,"Email is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPass)){
            Toast.makeText(this,"Password is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPass.length()<6){
            Toast.makeText(this,"Password should be more than 6 letters !",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(userMail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(loginActivity.this,"Login Successfully !",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(loginActivity.this,"Error "+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}