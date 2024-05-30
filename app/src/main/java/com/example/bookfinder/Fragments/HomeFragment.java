package com.example.bookfinder.Fragments;

import static com.example.bookfinder.Activities.ProfileActivity.lastScannedBookTitle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookfinder.Activities.ProfileActivity;
import com.example.bookfinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = view.findViewById(R.id.textView);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        Query checkBookInDataBase = null;
        if (ProfileActivity.lastScannedBookTitle != null)
        {
           checkBookInDataBase = reference.orderByChild("bookName").equalTo(lastScannedBookTitle);
        }

        if (checkBookInDataBase != null)
        {
            checkBookInDataBase.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        String summaryOfBook = snapshot.child(lastScannedBookTitle).child("resume").getValue(String.class);
                        String author = snapshot.child("author").getValue(String.class);
                        textView.setText(summaryOfBook);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }


        return view;
    }
}