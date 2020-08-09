package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.letstalk.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView contactedProfilePicture;
    TextView contactedUserName;
    Toolbar toolbar;
    String userId;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ImageButton sendButton;
    EditText messageTextBox;

    Intent intent;// To receive userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initialize();

        // to close chat in case of return
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageTextBox.getText().toString();
                if (!message.equals(""))
                    sendMessage(firebaseUser.getUid(), userId, message);
                else
                    Toast.makeText(MessageActivity.this, "You can't enter empty message", Toast.LENGTH_SHORT).show();
                //clear text box
                messageTextBox.setText("");
            }
        });
    }

    void initialize() {
        contactedUserName = findViewById(R.id.username_message);
        contactedProfilePicture = findViewById(R.id.profile_picture_message);
        sendButton = findViewById(R.id.send_button);
        messageTextBox = findViewById(R.id.enterMessage);

        toolbar = findViewById(R.id.tool_bar_message);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar(), "Error takes place in action bar").setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                contactedUserName.setText(Objects.requireNonNull(user, "Error in getting name").getUsername());
                if (user.getImageURL().equals("default")) {
                    contactedProfilePicture.setImageResource(R.mipmap.ic_launcher_round);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(contactedProfilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //This method used to send message and store them in database
    void sendMessage(String sender, String receiver, String message) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        databaseReference.child("Chats").push().setValue(hashMap);
    }
}