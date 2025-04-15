package com.example.photogallery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Uri> imageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);
        imageUris = fetchImagesFromGallery();

        com.example.photogallery.ImageAdapter adapter = new com.example.photogallery.ImageAdapter(this, imageUris);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            Intent intent = new Intent(GalleryActivity.this, com.example.photogallery.ImageDetailActivity.class);
            intent.putExtra("imageUri", imageUris.get(position).toString());
            startActivity(intent);
        });
    }

    private ArrayList<Uri> fetchImagesFromGallery() {
        ArrayList<Uri> uris = new ArrayList<>();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Images.Media._ID
        };

        try (Cursor cursor = getContentResolver().query(
                collection, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC")) {

            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri contentUri = Uri.withAppendedPath(collection, String.valueOf(id));
                    uris.add(contentUri);
                }
            }
        }
        return uris;
    }
}