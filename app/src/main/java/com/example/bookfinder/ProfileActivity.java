package com.example.bookfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookfinder.databinding.ActivityProfileBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Button buttonCapture;
    private TextView textViewData;
    private Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE = 100;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonCapture = findViewById(R.id.ocr_scann);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this);
            }
        });

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
                logoutPopUp();
            }
            return true;
        });
    }

    private void getTextFromImage(Bitmap bitmap)
    {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational())
        {
            Toast.makeText(this, "Error Ocurred !!!", Toast.LENGTH_SHORT);
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++)
            {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }

            textViewData.setText(stringBuilder.toString());
            buttonCapture.setText("Retake");

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle(stringBuilder);

            builder.setNegativeButton("No", (dialogInterface, i) -> {});
            builder.show();
        }
    }

    private void replaceGragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void logoutPopUp()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.setPositiveButton("Yes", (dialogInterface, i) -> finish());

        builder.show();
    }
}