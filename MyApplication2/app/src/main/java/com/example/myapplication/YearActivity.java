package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class YearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Year");
        Intent intent = getIntent();
        String semname = intent.getStringExtra("SemName");
        String departmentname = intent.getStringExtra("Department_name");
        FirebaseApp.initializeApp(this);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        StorageReference yearsRef = ref.child("chennai/" + semname + "/" + departmentname);
        ArrayList<String> files = new ArrayList<>();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) GridView gridView = findViewById(R.id.GRIDview);

        yearsRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference prefix : listResult.getPrefixes()) {
                        files.add(prefix.getName());
                        // All the prefixes under listRef.
                        // You may call listAll() recursively on them.

                    }
                    Collections.sort(files, Collections.reverseOrder());
                    YearAdapter adapter = new YearAdapter(YearActivity.this, files.toArray(new String[0]), semname, departmentname);
                    gridView.setAdapter(adapter);

                })
                .addOnFailureListener(e -> {
                    // Uh-oh, an error occurred!
                });
//        String[] buttonLabels = {"2023-2024", "2022-2023", "2021-2022", "2020-2021"};

    }

}
//
//}
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//        import android.widget.ScrollView;
//
//public class YearActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_year);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Year");
//        Intent intent = getIntent();
//        String semname = intent.getStringExtra("SemName");
//        String departmentname = intent.getStringExtra("DepartmentName");
//
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) GridView gridView = findViewById(R.id.GRIDview);
//        String[] buttonLabels;
//
//        if ("CSE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("CYS".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("AIE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("ECE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("CCE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("MEE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023", "2021-2022", "2020-2021"};
//        } else if ("ARE".equals(departmentname)) {
//            buttonLabels = new String[]{"2023-2024", "2022-2023"};
//        } else {
//            // Handle unknown department
//            buttonLabels = new String[]{};
//        }
//        YearAdapter adapter = new YearAdapter(this, buttonLabels,semname,departmentname);
//
//
//            gridView.setAdapter(adapter);
//        }
//    }

