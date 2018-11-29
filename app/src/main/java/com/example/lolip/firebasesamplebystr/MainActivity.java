package com.example.lolip.firebasesamplebystr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseStorage mDatabase = FirebaseStorage.getInstance();
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View imageContainer;
    private TextView overlayText;
    private TextView urlText;
    private ProgressBar progressBar;
    private Button uploadBtn;
    private Button switchBtn;
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }
    }
    private class InputTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            overlayText.setVisibility(s.length()>0 ? View.VISIBLE : View.INVISIBLE);
            overlayText.setText(s.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageContainer = findViewById(R.id.image_container);
        overlayText = findViewById(R.id.tv_overlay);
        overlayText.setText("");
        overlayText.setVisibility(View.INVISIBLE);
        EditText textInput = findViewById(R.id.editText);
        textInput.addTextChangedListener(new InputTextWatcher());
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        urlText = findViewById(R.id.tv_url);
        uploadBtn = findViewById(R.id.btn_uploadReport);
        uploadBtn.setOnClickListener(view->{
            imageContainer.setDrawingCacheEnabled(true);
            imageContainer.buildDrawingCache();
            Bitmap bitmap = imageContainer.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageContainer.setDrawingCacheEnabled(false);
            byte[] bytes = baos.toByteArray();
            String path = "firebaseImg/" + UUID.randomUUID() + ".png";
            StorageReference storageReference = mDatabase.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("text", overlayText.getText().toString())
                    .build();

            progressBar.setVisibility(View.VISIBLE);
            uploadBtn.setEnabled(false);
            UploadTask task = storageReference.putBytes(bytes, metadata);
            task.addOnSuccessListener(MainActivity.this, taskSnapshot -> {
                progressBar.setVisibility(View.GONE);
                uploadBtn.setEnabled(true);
                Log.d("main", "task successful");


            });
        });
        switchBtn = findViewById(R.id.btn_switch);
        switchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
        });



        verifyStoragePermissions(MainActivity.this);

    }
}
