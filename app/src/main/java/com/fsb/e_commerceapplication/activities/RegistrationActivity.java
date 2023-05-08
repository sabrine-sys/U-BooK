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
import com.fsb.e_commerceapplication.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    EditText name, email ,password;
    TextView signIn;
    FirebaseAuth auth ;
    FirebaseDatabase database;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://finaproject-cb027-default-rtdb.europe-west1.firebasedatabase.app");
        signUp = findViewById(R.id.reg_btn);
        progressBar = findViewById(R.id.progressbar_reg);
        progressBar.setVisibility(View.GONE);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        name = findViewById(R.id.name_reg);
        signIn = findViewById(R.id.sign_in_reg);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,loginActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createUser();

            }
        });
    }
    private void createUser(){
        String userName = name.getText().toString();
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Name is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }
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

        auth.createUserWithEmailAndPassword(userMail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserModel userModel = new UserModel(userName,userMail,userPass,"https://firebasestorage.googleapis.com/v0/b/finaproject-cb027.appspot.com/o/profile.png?alt=media&token=49557f63-9d36-4615-a10b-91274a9539bf");
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(userModel);
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    Toast.makeText(RegistrationActivity.this,"Registration successful !",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this,"Error , please try again !"+task.getException(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}