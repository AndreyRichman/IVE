package com.mta.ive.pages.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mta.ive.R;
import com.mta.ive.pages.home.HomeActivity;

public class SignUpInActivity extends AppCompatActivity {

    private SignInButton signInGmailButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG = "LoginActivity";
    private FirebaseAuth auth;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        signInGmailButton = findViewById(R.id.google_signin_button);


        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            updateUIFromGmail();
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            updateUIFromUser(user);
        }

        signInGmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGmail();
            }
        });

    }

    private void signInGmail() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(SignUpInActivity.this, "Signed In Succesfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            Toast.makeText(SignUpInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpInActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    updateUIFromUser(user);
                } else {
                    Toast.makeText(SignUpInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                    updateUIFromUser(null);
                }
            }
        });
    }

    private void updateUIFromGmail(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null) {
            String personName = account.getGivenName();
            String personEmail = account.getEmail();

            Toast.makeText(SignUpInActivity.this, "Welcome " + personName, Toast.LENGTH_SHORT).show();

            Intent homePage = new Intent(this, HomeActivity.class);
            homePage.putExtra("userName", personName);
            homePage.putExtra("email", personEmail);
            startActivity(homePage);
        }

    }
    private void updateUIFromUser(FirebaseUser user) {

        if (user != null) {
            String personName = user.getDisplayName();
            String personEmail = user.getEmail();

            Toast.makeText(SignUpInActivity.this, "Welcome " + personName, Toast.LENGTH_SHORT).show();

            Intent homePage = new Intent(this, HomeActivity.class);
            homePage.putExtra("userName", personName);
            homePage.putExtra("email", personEmail);
            startActivity(homePage);
        }

    }

    public void openSignUpPage(View btn) {
        Intent signUpPage = new Intent(this, SignUpActivity.class);
        startActivity(signUpPage);
    }

    public void openSignInPage(View btn) {
        Intent signInPage = new Intent(this, LoginActivity.class);
        startActivity(signInPage);
    }
}