package com.example.googleauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    TextView welcomeText;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = getIntent().getStringExtra("name");


        welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome! "+ name);
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());

        logoutBtn = findViewById(R.id.logoutBtn);



        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
            });
        });
    }
}