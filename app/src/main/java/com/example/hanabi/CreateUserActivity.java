package com.example.hanabi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateUserActivity extends Activity {

    private static final String TAG = "CreateUserActivity";

    EditText email, password, password2;
    Button signupButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.newEmail);
        password = (EditText) findViewById(R.id.newPassword);
        password2 = (EditText) findViewById(R.id.newPassword2);
        signupButton = (Button) findViewById(R.id.createUserButton);

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String s_email = email.getText().toString();
                String s_password = password.getText().toString();
                String s_password2 = password2.getText().toString();

                if(s_password.equals(s_password2)) {
                    Log.d(TAG, s_email + " " + s_password);

                    firebaseAuth.createUserWithEmailAndPassword(s_email, s_password)
                            .addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "onComplete");
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");

                                        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(CreateUserActivity.this, "회원가입 완료!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CreateUserActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            })
                            .addOnFailureListener(CreateUserActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure");
                                }
                            });
                }
                else
                {
                    Toast.makeText(CreateUserActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}