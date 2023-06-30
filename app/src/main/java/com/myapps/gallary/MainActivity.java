package com.myapps.gallary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv;
    private ImageButton deletebtn;
    private ProgressBar progressBar;
    private ArrayList<String> list;
    private GalleryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv = findViewById(R.id.recyclerview);
        deletebtn = findViewById(R.id.deletebtn);
        progressBar = findViewById(R.id.progressBar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("images");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();

                for (DataSnapshot shot : snapshot.getChildren()){
                    String data = Objects.requireNonNull(shot.getValue()).toString();
                    list.add(data);
                }
                rcv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                Collections.shuffle(list);
                adapter = new GalleryAdapter(list, MainActivity.this);
                rcv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}