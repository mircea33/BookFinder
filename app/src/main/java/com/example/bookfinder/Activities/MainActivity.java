package com.example.bookfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.bookfinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private Button addToFavouritesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.text_view_main);
        addToFavouritesButton = findViewById(R.id.add_to_favourites);

        Intent intent = getIntent();
        String summary = intent.getStringExtra("summary");
        String author = intent.getStringExtra("author");
        String bookTitle = intent.getStringExtra("bookTitle");
        String username = intent.getStringExtra("username");

        addElementToDBInUser(username, bookTitle, "Library");

        addToFavouritesButton.setOnClickListener(view ->
        {
            addElementToDBInUser(username, bookTitle, "Favourites");
            Toast.makeText(MainActivity.this, "The book has been added to favourites!", Toast.LENGTH_SHORT).show();
        });

        textView.setText(bookTitle + " by " + author + "\n Summary: \n" + summary);
    }

    private void addElementToDBInUser(String userName, String bookTitle, String tabelName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    DatabaseReference userRef = snapshot.getChildren().iterator().next().getRef();
                    List<String> library = getValuesInTabel(userName, tabelName);
                    library.add(bookTitle);
                    userRef.child(tabelName).setValue(library);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private List<String> getValuesInTabel(String username, String tabelName)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        DocumentReference userDocRef = usersRef.document(username);
        List<String> valuesInTabel = new ArrayList<>();
        userDocRef.get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
               DocumentSnapshot document = task.getResult();
               if (document.exists())
               {
                    List<String> tabelInuser = (List<String>) document.get(tabelName);
                    if (tabelInuser != null)
                    {
                        for (String book : tabelInuser)
                        {
                            tabelInuser.add(book);
                        }
                    }
                }
            }
        });

        return valuesInTabel;
    }
}