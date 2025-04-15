package com.example.photogallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView imageInfo;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imageView = findViewById(R.id.imageView);
        imageInfo = findViewById(R.id.imageInfo);
        deleteBtn = findViewById(R.id.deleteBtn);

        String imageUriStr = getIntent().getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriStr);

        Glide.with(this).load(imageUri).into(imageView);

        Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            imageInfo.setText("Name: " + name + "\nPath: " + path + "\nSize: " + size + " bytes\nDate Taken: " + date);
            cursor.close();
        }

        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                        getContentResolver().delete(imageUri, null, null);
                        Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
