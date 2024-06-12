package com.example.bookfinder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookfinder.Entities.User;
import com.example.bookfinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view ->
        {
            if (!validateUsername() | !validatePassword())
            {
            }
            else
            {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    public Boolean validateUsername()
    {
        String val = loginUsername.getText().toString();
        if (val.isEmpty())
        {
            loginUsername.setError("Username cannot be empty");
            return false;
        }
        else
        {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword()
    {
        String val = loginPassword.getText().toString();
        if (val.isEmpty())
        {
            loginPassword.setError("Password cannot be empty");
            return false;
        }
        else
        {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser()
    {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword))
                    {
                        loginUsername.setError(null);

                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        List<String> favouritesValues = (List<String>) snapshot.child(userUsername).child("Favourites").getValue();
                        List<String> library = (List<String>) snapshot.child(userUsername).child("Library").getValue();

                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.putExtra("name", usernameFromDB);
                        intent.putExtra("Favourites", getElementes(favouritesValues));
                        intent.putExtra("Library", getElementes(library));
                        startActivity(intent);
                    }
                    else
                    {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                }
                else
                {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private String getElementes(List<String> books)
    {
        String bookSeparatedByComma = "";

        for (String book : books)
        {
            if (bookSeparatedByComma.isEmpty())
            {
                bookSeparatedByComma = book;
                continue;
            }

            bookSeparatedByComma = bookSeparatedByComma + "," + book;
        }

        return bookSeparatedByComma;
    }
}