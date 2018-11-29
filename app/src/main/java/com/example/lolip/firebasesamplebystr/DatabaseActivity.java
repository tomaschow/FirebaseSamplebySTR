package com.example.lolip.firebasesamplebystr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseActivity extends AppCompatActivity {
    private static final String TAG ="DatabaseActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mRef = database.getReference("report");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databases);

        Button btnSwitch = findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        Button btnUpload = findViewById(R.id.btn_uploadReport);
        btnUpload.setOnClickListener(view -> {
            ReportPOJO reportPojo = uploadReport();
            Log.d(TAG, "Uploaded report: " + reportPojo.toString());
        });
        Button btnDownload = findViewById(R.id.btn_downloadReport);
        btnDownload.setOnClickListener(view -> {

        });

        // Communicating with firebase database

//        mRef.setValue("A blue purse");
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value = " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Failed reading value");
//            }
//        });
    }

    ReportPOJO uploadReport(){
        //Gson gson = new Gson();
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> photos = new ArrayList<>();
        tags.add("Purse");
        tags.add("Wallet");
        photos.add("https://www.url.com/photo1");
        photos.add("https://www.url2.com/photo2"); // Need to upload photos first to get the url
        ReportPOJO pojo = new ReportPOJO();
//        pojo.setId();
        pojo.setLatitude(23.333333);
        pojo.setLongitude(-32.322222);
        pojo.setPhoneNum("15089992839");
        pojo.setTags(tags);
        pojo.setPhotos(photos);
        pojo.setTimestamp(new Date());

        DatabaseReference ref = database.getReference("reports");
        ref.push().setValue(pojo);

        return pojo;

    }
}
