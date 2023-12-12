package com.sain.projectlunixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, skill1,skill2,skill3,ultimate;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String id = "";
    String AgentId;
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        skill1= findViewById(R.id.skill1);
        skill2= findViewById(R.id.skill2);
        skill3= findViewById(R.id.skill3);
        ultimate = findViewById(R.id.ultimate);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);


        Bundle bundle = getIntent().getExtras();
        String AgentName = bundle.getString("Title");

        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            skill1.setText("Skill 1 : "+ bundle.getString("Skill1"));
            skill2.setText("Skill 2 : "+bundle.getString("Skill2"));
            skill3.setText("Skill 3 : "+bundle.getString("Skill3"));
            ultimate.setText("Skill Ultimate : "+bundle.getString("ultimate"));
            AgentId = bundle.getString("id");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance("https://project-mco-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("agent");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                reference.child(AgentName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        if(Objects.equals(imageUrl, "")){
                            Toast.makeText(DetailActivity.this,"deleted",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();
                        } else {
                            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this,"deleted",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        finish();
                    }
                });
                        }
                    }
                });
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateAgent.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("skill1",skill1.getText().toString())
                        .putExtra("skill2",skill2.getText().toString())
                        .putExtra("skill3",skill3.getText().toString())
                        .putExtra("ultimate",ultimate.getText().toString())
                        .putExtra("id", AgentId);
                startActivity(intent);
            }
        });
    }
}