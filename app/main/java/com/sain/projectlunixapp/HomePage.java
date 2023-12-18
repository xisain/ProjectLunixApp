package com.sain.projectlunixapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://project-mco-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("agent");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("agent_images");
    RecyclerView recyclerView;
    SearchView search;
    List<Agent> dataList;
    MyAdapter adapter;
    DatabaseReference databaseReference;
    FloatingActionButton fab;
    ImageView logout;
    private DrawerLayout HomePageDrawer;
    private Toolbar toolbar;
    private NavigationView SideContentViewDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //add agent button link from homepage
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,AddAgent.class);
                startActivity(intent);

            }
        });
        search = findViewById(R.id.search);
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

        logout = findViewById(R.id.login);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // DrawerLayout
        HomePageDrawer = findViewById(R.id.home_page);

        // NavigationView
        SideContentViewDrawer = findViewById(R.id.sidecontentview);
        setupDrawerContent(SideContentViewDrawer);

        //Nav icon
        ImageView navBarIcon = findViewById(R.id.navBarIcon);
        navBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageDrawer.openDrawer(GravityCompat.START);
            }
        });

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomePage.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new MyAdapter(HomePage.this, dataList);
        recyclerView.setAdapter(adapter);

        // Retrieve data from Realtime Database
        databaseReference = ref; // You can modify this to point to a specific child if needed
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Agent dataClass = itemSnapshot.getValue(Agent.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }

    //Sidebar
    private void setupDrawerContent(NavigationView SideContentViewDrawer) {
        SideContentViewDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.homeP) {
            // Navigasikan ke layout HomeP
            setContentView(R.layout.activity_home_page);
        } else if (itemId == R.id.agentadd) {
            // Navigasikan ke layout AgentAdd
            setContentView(R.layout.activity_add_agent);
        } else if (itemId == R.id.logout) {
            // Navigasikan ke layout Logout
            setContentView(R.layout.activity_main);
        } else if (itemId == R.id.detail) {
            // Navigasikan ke layout default, misalnya HomeP
            setContentView(R.layout.activity_detail);
        }

        item.setChecked(true);
        setTitle(item.getTitle());

        HomePageDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HomePageDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
