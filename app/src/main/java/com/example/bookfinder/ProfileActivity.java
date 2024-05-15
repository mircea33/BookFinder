package com.example.bookfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookfinder.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private boolean continueLogOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceGragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homeMenu)
            {
                replaceGragment(new HomeFragment());
            }
            else if (itemId == R.id.favourites)
            {
                replaceGragment(new FavoritesFragment());
            }
            else if (itemId == R.id.library)
            {
                replaceGragment(new LibraryFragment());
            }
            else if (itemId == R.id.logout)
            {
                if (logoutPopUp())
                {
                    finish();
                }
            }
            return true;
        });
    }

    private void replaceGragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private boolean logoutPopUp()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        builder.setNegativeButton("No", (dialogInterface, i) -> continueLogOut = false);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> continueLogOut = true);

        builder.show();
        return continueLogOut;
    }
}