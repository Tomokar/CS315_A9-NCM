package com.example.cs315_a9_ncm;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity
{
    private EditText emailTextView, passwordTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Button btn = findViewById(R.id.login);
        Button btn2 = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        // Set on Click Listener on Sign-in button
        btn.setOnClickListener(v -> loginUserAccount());
        btn2.setOnClickListener(v -> registerNewUser());
    }

    private void loginUserAccount()
    {
        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }

        // sign-in existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);

                // if sign-in is successful
                // intent to home activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                // sign-in failed
                Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void registerNewUser()
    {
        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        String[] arStr = email.split("@");
        String displayName = arStr[0];

        // Validations for input email and password
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();

                assert fUser != null;
                fUser.updateProfile(profileUpdates).addOnCompleteListener(task1 ->
                {
                    if(task1.isSuccessful())
                    {
                        Log.d(TAG, "Update successful");
                    }
                });

                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);

                // if the user created intent to login activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                // Registration failed
                Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}