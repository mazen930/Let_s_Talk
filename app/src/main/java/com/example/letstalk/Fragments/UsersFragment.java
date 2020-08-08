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

public class UsersFragment extends Fragment {
    private RecyclerView usersRecycleView;
    private UserAdapter userAdapter;
    private ArrayList<User> usersArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        initialize(view);
        return view;
    }

    void initialize(View view) {
        usersRecycleView = view.findViewById(R.id.users_list_fragment);
        usersRecycleView.setHasFixedSize(true);
        usersRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersArrayList = new ArrayList<>();
        //care you must add these lines here after list is updated so that recycle view will be notified when it change or use it like  iam using it
        // and notify on data set changed
        userAdapter = new UserAdapter(getContext(), usersArrayList);
        usersRecycleView.setAdapter(userAdapter);
        readUsers();

    }

    void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    assert currentUser != null;
                    assert firebaseUser != null;
                    if (!currentUser.getId().equals(firebaseUser.getUid())) {
                        usersArrayList.add(currentUser);
                    }
                }
                // to notify recycle view that list has been changed
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}