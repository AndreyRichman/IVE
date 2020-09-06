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
import com.mta.ive.R;
import com.mta.ive.pages.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {



    private Button signInRegularBtn;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        this.emailEditText = findViewById(R.id.editTextTextEmailAddress);
        this.passwordEditText = findViewById(R.id.editTextTextPassword);
//        TextView title = (TextView)findViewById(R.id.title);
//        title.setText(getIntent().getStringExtra("PAGE_NAME"));

//        signInGmailButton = findViewById(R.id.google_signin_button);
        signInRegularBtn = findViewById(R.id.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();


        signInRegularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInRegular();
            }
        });

    }

    private void signInRegular(){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Missing Email", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Missing Password", Toast.LENGTH_SHORT);
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user){
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (user != null){
//            String personName = user.getDisplayName(); //.getGivenName();
            String email = user.getEmail();
//            String personId = account.getId();

            Toast.makeText(LoginActivity.this, "Welcome " + email, Toast.LENGTH_SHORT).show();

            Intent homePage = new Intent(this, HomeActivity.class);
            homePage.putExtra("email", email);
            startActivity(homePage);
        }
    }

//    private void signInGmail(){
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == RC_SIGN_IN){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            Toast.makeText(LoginActivity.this, "Signed In Succesfully", Toast.LENGTH_SHORT).show();
//            FirebaseGoogleAuth(account);
//        }
//        catch (ApiException e){
//            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//            FirebaseGoogleAuth(null);
//        }
//    }
//
//    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
//        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(LoginActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
//                    FirebaseUser user = auth.getCurrentUser();
//                    updateUI(user);
//                }
//                else {
//                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//            }
//        });
//    }
//
//    private void updateUI(FirebaseUser user){
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//
//        if (account != null){
//            String personName = account.getGivenName();
//            String personEmail = account.getEmail();
//            String personId = account.getId();
//
//            Toast.makeText(LoginActivity.this, "Welcome " + personName, Toast.LENGTH_SHORT).show();
//
//            Intent homePage = new Intent(this, HomeActivity.class);
//            homePage.putExtra("userName", personName);
//            startActivity(homePage);
//        }
//    }
}