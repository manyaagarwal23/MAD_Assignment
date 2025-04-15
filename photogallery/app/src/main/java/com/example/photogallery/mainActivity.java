package com.example.photogallery;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class mainActivity extends AppCompatActivity {
    Button takePhotoBtn, viewGalleryBtn;
    Uri cameraImageUri;
    public static ArrayList<Uri> selectedUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        viewGalleryBtn = findViewById(R.id.viewGalleryBtn);

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                1
        );

        ActivityResultLauncher<Uri> takePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                    if (success && cameraImageUri != null) {
                        selectedUris.add(cameraImageUri);
                    }
                });

        takePhotoBtn.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            cameraImageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
            );
            takePictureLauncher.launch(cameraImageUri);
        });

        viewGalleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity.this, com.example.photogallery.GalleryActivity.class);
            startActivity(intent);
        });
    }
}
