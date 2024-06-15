package com.example.bookfinder.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookfinder.Fragments.FavoritesFragment;
import com.example.bookfinder.Fragments.HomeFragment;
import com.example.bookfinder.Fragments.LibraryFragment;
import com.example.bookfinder.R;
import com.example.bookfinder.databinding.ActivityProfileBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FloatingActionButton buttonCapture;
    private Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE = 100;
    private String lastScannedBookTitle;
    public static String favourites;
    public static String library;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (library == null || favourites == null)
        {
            favourites = intent.getStringExtra("FavouritesFromLogin");
            library = intent.getStringExtra("LibraryFromLogin");
        }


        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonCapture = findViewById(R.id.ocr_scann);

        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }
        binding.bottomNavigationView.setBackground(null);

        buttonCapture.setOnClickListener((v) -> CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this));

        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            int itemId = item.getItemId();
            if (itemId == R.id.homeMenu)
            {
                replaceFragment(new HomeFragment());
            }
            else if (itemId == R.id.favourites)
            {
                replaceFragment(new FavoritesFragment(intent.getStringExtra("name"), favourites));
            }
            else if (itemId == R.id.library)
            {
                replaceFragment(new LibraryFragment(intent.getStringExtra("name"), library));
            }
            else if (itemId == R.id.logout)
            {
                logoutPopUp();
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null)
            {
                Uri resultUri = result.getUri();
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
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

            lastScannedBookTitle = stringBuilder.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("Is the title of the book ?");
            builder.setMessage(stringBuilder);

            builder.setPositiveButton("yes", (dialogInterface, i) -> checkIfBookIsPresentInDb());
            builder.setNegativeButton("no", ((dialogInterface, i) -> wouldYouLikeToRetakeThePicture()));

            builder.show();
        }
    }

    private void checkIfBookIsPresentInDb()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        Query checkBookInDataBase = reference.orderByChild("bookName").equalTo(lastScannedBookTitle.trim());
        checkBookInDataBase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String summaryOfBook = snapshot.child(lastScannedBookTitle.trim()).child("resume").getValue(String.class);
                    String author = snapshot.child(lastScannedBookTitle.trim()).child("author").getValue(String.class);
                    String genre = snapshot.child(lastScannedBookTitle.trim()).child("genre").getValue(String.class);

                    String username = getIntent().getStringExtra("name");

                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.putExtra("summary", summaryOfBook);
                    intent.putExtra("author", author);
                    intent.putExtra("bookTitle", lastScannedBookTitle);
                    intent.putExtra("genre", genre);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("We are sorry the book you are looking for is not present in our DB");
                    builder.setNegativeButton("OK", (dialogInterface, i) -> {});
                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void wouldYouLikeToRetakeThePicture()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Would you like to retake the photo ?");
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.setPositiveButton("Yes", (dialogInterface, i) ->  CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this));
        builder.show();
    }

    private void replaceFragment(Fragment fragment)
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

        builder.setPositiveButton("Yes", (dialogInterface, i) -> finish());
        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.show();
        library = null;
        favourites= null;
    }
}