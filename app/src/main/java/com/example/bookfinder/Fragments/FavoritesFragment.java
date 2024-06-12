package com.example.bookfinder.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bookfinder.Activities.MainActivity;
import com.example.bookfinder.Activities.ProfileActivity;
import com.example.bookfinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment
{
    private String username;
    private ListView listView;
    private List<String> listOfFavourites = new ArrayList<>();

    public FavoritesFragment(String username)
    {
        super();
        this.username = username;
        MainActivity.getValuesInTabelFromUser(listOfFavourites, username, "Favourites", elementsInTabel ->  {});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        listView = view.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfFavourites);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> checkIfBookIsPresentInDb(listOfFavourites.get(position)));
        return view;
    }


    private void checkIfBookIsPresentInDb(String selectedItem)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        Query checkBookInDataBase = reference.orderByChild("bookName").equalTo(selectedItem.trim());
        checkBookInDataBase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String summaryOfBook = snapshot.child(selectedItem.trim()).child("resume").getValue(String.class);
                    String author = snapshot.child(selectedItem.trim()).child("author").getValue(String.class);
                    String genre = snapshot.child(selectedItem.trim()).child("genre").getValue(String.class);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("summary", summaryOfBook);
                    intent.putExtra("author", author);
                    intent.putExtra("bookTitle", selectedItem);
                    intent.putExtra("genre", genre);
                    intent.putExtra("username", username);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}