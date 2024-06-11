package com.example.bookfinder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookfinder.Entities.User;
import com.example.bookfinder.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener((view) ->
                {

                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    EditText[] editTextBoxes = new EditText[]{signupName, signupEmail, signupUsername, signupPassword};
                     if (isAlertRequired(editTextBoxes))
                     {
                         return;
                     }

                    String name = signupName.getText().toString();
                    String email = signupEmail.getText().toString();
                    String username = signupUsername.getText().toString();
                    String password = signupPassword.getText().toString();

                    if (!email.contains("@"))
                    {
                        signupEmail.setError("Invalid credentials");
                       signupEmail.requestFocus();
                       return;
                    }

                     User user = new User(name, email, username, password);
                    reference.child(username).setValue(user);

                     Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
            }
        );

        loginRedirectText.setOnClickListener((view) ->
        {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
        });
    }

    private boolean isAlertRequired(EditText[] editTexts)
    {
        boolean alertRequired = false;
        for (EditText editText : editTexts)
        {
            if (editText.getText().toString().isEmpty())
            {
                editText.setError("Invalid credentials");
                editText.requestFocus();
                alertRequired = true;
            }
        }

        return alertRequired;
    }
}