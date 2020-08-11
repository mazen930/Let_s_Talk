package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.letstalk.Adapters.MessageAdapter;
import com.example.letstalk.Models.Chat;
import com.example.letstalk.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    ArrayList<Chat> messagesArrayList;
    RecyclerView chatRecyclerView;
    MessageAdapter messageAdapter;

    ValueEventListener seenValueEventListener;

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
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

        chatRecyclerView = findViewById(R.id.message_list);
        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

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
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(contactedProfilePicture);
                }
                readMessages(firebaseUser.getUid(), userId, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userId);
    }

    void seenMessage(final String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (Objects.requireNonNull(chat, "Chat object is null").getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //This method used to send message and store them in database
    void sendMessage(final String sender, String receiver, String message) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("seen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        // add user to chat fragment directly
        final DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid())
                .child(userId);
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatReference.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readMessages(final String myId, final String userId, final String imgURL) {
        messagesArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat currentMessage = dataSnapshot.getValue(Chat.class);
                    assert currentMessage != null;
                    if (currentMessage.getReceiver().equals(myId) && currentMessage.getSender().equals(userId) ||
                            currentMessage.getReceiver().equals(userId) && currentMessage.getSender().equals(myId)) {
                        messagesArrayList.add(currentMessage);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this, messagesArrayList, imgURL);
                chatRecyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void status(String status) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        databaseReference.updateChildren(hashMap);
    }

    // this two methods are overridden related to activity life cycle
    // here is link for more understanding :
    // https://developer.android.com/guide/components/activities/activity-lifecycle#:~:text=Use%20the%20onPause()%20method,you%20expect%20to%20resume%20shortly.

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //disable listener when activity is paused
        databaseReference.removeEventListener(seenValueEventListener);
        status("offline");
    }

}