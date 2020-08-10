package com.example.letstalk.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letstalk.Adapters.UserAdapter;
import com.example.letstalk.Models.Chat;
import com.example.letstalk.Models.User;
import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class ChatFragment extends Fragment {
    RecyclerView chatList;
    UserAdapter userAdapter;
    ArrayList<User> userArrayList;
    ArrayList<String> userIdArrayList;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initialize(view);
        return view;
    }

    void initialize(View view) {
        // set has fixed size when you know that data won't change frequently
        chatList = view.findViewById(R.id.chatted_with_recycle_view);
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        userIdArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userArrayList);
        chatList.setAdapter(userAdapter);
        userAdapter = new UserAdapter(getContext(), userArrayList);
        chatList.setAdapter(userAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIdArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (Objects.requireNonNull(chat, "No data found").getSender().equals(firebaseUser.getUid())) {
                        userIdArrayList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())) {
                        userIdArrayList.add(chat.getSender());
                    }
                }
                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readChat() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // using hashSet is better than using a lot of nested loops that will increase the complexity in case of large connects
                HashSet<String> userHashSet = new HashSet<>(userIdArrayList);
                for (String id : userHashSet) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        if (id.equals(user.getId())) {
                            userArrayList.add(user);
                            break; // we can break here as we found a target and no longer need to continue
                        }
                    }
                }
                // again this is used to notify adapter that data set is changed
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


/*
// using hashSet is better than using a lot of nested loops that will increase the complexity in case of large connects
HashSet<User> userHashSet = new HashSet<>();
                for (String id : userIdArrayList) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
        assert user != null;
        if (id.equals(user.getId())) {
        userHashSet.add(user);
        break; // we can break here as we found a target and no longer need to continue
        }
        }
        }
        userArrayList = new ArrayList<>(userHashSet);
        userAdapter = new UserAdapter(getContext(), userArrayList);
        chatList.setAdapter(userAdapter);*/


/*
// another approach is using hashSet cause it is  better than using a lot of nested loops that will increase the complexity in case of large connects
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);

        for (String id : userIdArrayList) {
        // this inner for loop to avoid repetition of users in list
        assert user != null;
        if (user.getId().equals(id)) {
        if (userArrayList.size() != 0) {
        for (User user1 : userArrayList) {
        if (!user.getId().equals(user1.getId())) {
        userArrayList.add(user);
        }
        }
        } else { // In case of empty list no need to check
        userArrayList.add(user);
        }
        }
        }
        }
        userAdapter.notifyDataSetChanged();*/
