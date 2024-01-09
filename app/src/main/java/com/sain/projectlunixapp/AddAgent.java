package com.sain.projectlunixapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
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

public class AddAgent extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    TextInputEditText agent_name, desc, skill1, skill2, skill3, ultimate;
    ImageView uploadimg;
    Button addAgent;
    Uri imageUri; // Uri for storing the selected image URI
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://project-mco-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = database.getReference("agent");
    String key = database.getReference("agent").push().getKey();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("agent_images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);

        agent_name = findViewById(R.id.Agent_name);
        uploadimg = findViewById(R.id.agent_img);
        desc = findViewById(R.id.description);
        skill1 = findViewById(R.id.skill1);
        skill2 = findViewById(R.id.skill2);
        skill3 = findViewById(R.id.skill3);
        ultimate = findViewById(R.id.ultimate);
        addAgent = findViewById(R.id.AddAgent);

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        addAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String agent, dsc, sk1, sk2, sk3, ulti;
                agent = String.valueOf(agent_name.getText());
                dsc = String.valueOf(desc.getText());
                sk1 = String.valueOf(skill1.getText());
                sk2 = String.valueOf(skill2.getText());
                sk3 = String.valueOf(skill3.getText());
                ulti = String.valueOf(ultimate.getText());

                if (TextUtils.isEmpty(agent) || TextUtils.isEmpty(dsc) || TextUtils.isEmpty(sk1) ||
                        TextUtils.isEmpty(sk2) || TextUtils.isEmpty(sk3) || TextUtils.isEmpty(ulti)) {
                    Toast.makeText(AddAgent.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri != null) {
                    uploadImageAndSaveAgent(key,agent, dsc, sk1, sk2, sk3, ulti);
                } else {
                    // If no image is selected, save agent without an image
                    saveAgentToDatabase(key,agent, dsc, sk1, sk2, sk3, ulti, "");
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadimg.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveAgent(String key,String agent, String dsc, String sk1, String sk2, String sk3, String ulti) {
        // Ensure the agent name is not empty
        if (TextUtils.isEmpty(agent)) {
            Toast.makeText(AddAgent.this, "Agent name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a reference with the desired filename based on the agent's name
        StorageReference fileRef = storageRef.child(agent + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveAgentToDatabase(key, agent, dsc, sk1, sk2, sk3, ulti, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddAgent.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Image upload failed", e);
                });
    }
    // Testing
    private void saveAgentToDatabase(String key, String agent, String dsc, String sk1, String sk2, String sk3, String ulti, String imageUrl) {
        DatabaseReference agentRef = ref.child(agent);
        agentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(AddAgent.this, "Agent with the same name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Agent newAgent = new Agent(key, agent,dsc, imageUrl, new Skill(sk1, sk2, sk3, ulti));
                    agentRef.setValue(newAgent);
                    Toast.makeText(AddAgent.this, "Agent added successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking agent existence", error.toException());
            }
        });
    }
}
