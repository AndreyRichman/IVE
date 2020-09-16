package com.mta.ive.pages.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mta.ive.MainActivity;
import com.mta.ive.R;
import com.mta.ive.pages.home.HomeActivity;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpBtn;
    private EditText userNameEditText, emailEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;

    String userName, email, password;

    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        this.userNameEditText = findViewById(R.id.editTextTextPersonName);
        this.emailEditText = findViewById(R.id.editTextTextEmailAddress);
        this.passwordEditText = findViewById(R.id.editTextTextPassword);
        this.signUpBtn = findViewById(R.id.sign_up);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            updateUI(user);
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp(){
        this.userName = userNameEditText.getText().toString();
        this.email = emailEditText.getText().toString().trim();
        this.password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(SignUpActivity.this, "Missing Name", Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(email)){
            Toast.makeText(SignUpActivity.this, "Missing Email", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6){
            Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT);
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if (user != null) {
            Intent homePage = new Intent(this, MainActivity.class);
            homePage.putExtra("userName", this.userName);
            homePage.putExtra("email", user.getEmail());

            startActivity(homePage);
        }
    }
}