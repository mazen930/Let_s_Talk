package com.example.letstalk.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.letstalk.Adapters.UserAdapter;
import com.example.letstalk.Models.User;
import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private RecyclerView usersRecycleView;
    private UserAdapter userAdapter;
    private ArrayList<User> usersArrayList;
    private EditText searchBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        initialize(view);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    void initialize(View view) {
        searchBox = view.findViewById(R.id.search_user);

        usersRecycleView = view.findViewById(R.id.users_list_fragment);
        usersRecycleView.setHasFixedSize(true);
        usersRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersArrayList = new ArrayList<>();
        //care you must add these lines here after list is updated so that recycle view will be notified when it change or use it like  iam using it
        // and notify on data set changed
        userAdapter = new UserAdapter(getContext(), usersArrayList, false);
        usersRecycleView.setAdapter(userAdapter);
        readUsers();

    }

    // to search for users in firebase
    private void searchUser(String word) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().
                getReference("Users").orderByChild("search").
                startAt(word).endAt(word + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        usersArrayList.add(user);
                    }
                }
                // Instead of created new adapter better behavioural is use below function
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get all users only if there is no input in edit box
                if (searchBox.getText().toString().equals("")) {
                    usersArrayList.clear();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}