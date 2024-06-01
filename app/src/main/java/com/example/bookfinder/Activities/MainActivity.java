package com.example.bookfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookfinder.R;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
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
        Intent intent = getIntent();
        String summary = intent.getStringExtra("summary");
        String author = intent.getStringExtra("author");
        String bookTitle = intent.getStringExtra("bookTitle");

        textView.setText(bookTitle + " by " + author + "/n Summary: /n" + summary);
    }
}