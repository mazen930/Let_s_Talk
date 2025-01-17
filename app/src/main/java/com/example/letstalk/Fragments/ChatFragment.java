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
import com.example.letstalk.Models.ChatList;
import com.example.letstalk.Models.User;
import com.example.letstalk.Notifications.Token;
import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private UserAdapter userAdapter;
    private ArrayList<User> userArrayList;
    private ArrayList<ChatList> userIdArrayList;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        initialize(view);
        UpdateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize(view);
    }

    void initialize(View view) {
        // set has fixed size when you know that data won't change frequently
        RecyclerView chatList = view.findViewById(R.id.chatted_with_recycle_view);
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        userIdArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userArrayList, true);
        chatList.setAdapter(userAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIdArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    userIdArrayList.add(chatList);
                }
                ChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void UpdateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void ChatList() {
        userArrayList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for (ChatList chatList1 : userIdArrayList) {
                        assert user != null;
                        if (user.getId().equals(chatList1.getId())) {
                            userArrayList.add(user);
                        }
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
