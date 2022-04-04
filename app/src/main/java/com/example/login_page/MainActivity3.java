package com.example.login_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    EditText mEmail,mPassword,mretypePassword;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mEmail=findViewById(R.id.editTextTextEmailAddress);
        mPassword=findViewById(R.id.editTextTextPassword);
        mretypePassword=findViewById(R.id.editTextTextPassword3);
        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Button register=findViewById(R.id.button);
        register.setOnClickListener(view -> {
            String email=mEmail.getText().toString().trim();
            String password=mPassword.getText().toString().trim();
            String repassword=mretypePassword.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                mEmail.setError("E-mail is Required.");
                return;
            }
            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password is Required.");
                return;
            }
            if(TextUtils.isEmpty(repassword)){
                mretypePassword.setError("Re-Type Password is Required.");
                return;
            }
            if(password.length()<6){
                mPassword.setError("Password length is more than 6 characters.");
                return;
            }
            if(repassword.length()<6){
                mretypePassword.setError("Password length is more than 6 characters.");
                return;
            }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity3.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            userID=firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fstore.collection("users").document(userID);
                            Map<String,Object> user =new HashMap<>();
                            user.put("email",email);
                            user.put("password",password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Tag","onSuccess: user profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(@NonNull Exception e){
                                    Log.d("Tag","OnFailure: "+e.toString());
                                }
                            });
                            openMainActivity();
                        } else {
                            Toast.makeText(MainActivity3.this, "Error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        });

    }

   

    public void openMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}