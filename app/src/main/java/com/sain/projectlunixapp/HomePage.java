package com.sain.projectlunixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sain.projectlunixapp.model.Agent;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    FloatingActionButton fab;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://project-mco-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("agent");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("agent_images");
    RecyclerView recyclerView;
    List<Agent> dataList;
    MyAdapter adapter;
    SearchView search;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        search = findViewById(R.id.search);
        search.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomePage.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(HomePage.this, dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = ref;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Agent dataClass = itemSnapshot.getValue(Agent.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                    dialog.dismiss();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AddAgent.class);
                startActivity(intent);
            }
        });

    }
    public void searchList(String text) {
        ArrayList<Agent> searchList = new ArrayList<>();
        for(Agent dataClass: dataList){
            if(dataClass.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}
