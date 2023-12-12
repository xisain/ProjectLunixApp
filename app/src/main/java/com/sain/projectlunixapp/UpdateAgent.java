package com.sain.projectlunixapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sain.projectlunixapp.model.Agent;
import com.sain.projectlunixapp.model.Skill;

public class UpdateAgent extends AppCompatActivity {
    String originalAgentName;
    String AgentName, description , skill1, skill2, skill3, ultimate;
    String imageURL, key;
    ImageView updateImage;
    Button updateButton;
    EditText updateName,updateDesc, updateSkill1, updateSkill2, updateSkill3, updateUltimate;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_agent);
        updateButton = findViewById(R.id.updateButton);
        updateName = findViewById(R.id.UpdateName);
        updateDesc = findViewById(R.id.UpdateDesc);
        updateSkill1 = findViewById(R.id.updateSkill1);
        updateSkill2 = findViewById(R.id.updateSkill2);
        updateSkill3 = findViewById(R.id.updateSkill3);
        updateUltimate = findViewById(R.id.updateUltimate);
        updateImage = findViewById(R.id.updateImage);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Glide.with(UpdateAgent.this).load(bundle.getString("Image")).into(updateImage);
            AgentName = bundle.getString("Title");
            updateName.setText(AgentName);
            updateDesc.setText(bundle.getString("Description"));
            updateSkill1.setText(bundle.getString("skill1"));
            updateSkill2.setText(bundle.getString("skill2"));
            updateSkill3.setText(bundle.getString("skill3"));
            updateUltimate.setText(bundle.getString("ultimate"));
            originalAgentName = bundle.getString("Title");
            Log.d("TRYKEY" ,bundle.getString("id"));
            key = bundle.getString("id");
            imageURL = bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance("https://project-mco-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("agent").child(originalAgentName);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgentName = updateName.getText().toString();
                description = updateDesc.getText().toString();
                skill1 = updateSkill1.getText().toString();
                skill2 = updateSkill2.getText().toString();
                skill3 = updateSkill3.getText().toString();
                ultimate = updateUltimate.getText().toString();
                key = key;
                imageURL = imageURL;

                DatabaseReference newReference = databaseReference.getParent().child(AgentName);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        newReference.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if(error != null){
                                    Toast.makeText(UpdateAgent.this, "Failed to rename the node", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Agent newAgent = new  Agent( key,AgentName, description, imageURL, new Skill(skill1,skill2,skill3,ultimate));
                                       newReference.setValue(newAgent);
                                        Toast.makeText(UpdateAgent.this, "Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateAgent.this, HomePage.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firebase", "Error removing the original node", e);
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error renaming the node", error.toException());
                    }
                });



            }
        });
    }

}
