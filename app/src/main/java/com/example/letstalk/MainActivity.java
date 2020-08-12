package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.letstalk.Fragments.APIService;
import com.example.letstalk.Fragments.ChatFragment;
import com.example.letstalk.Fragments.ProfileFragment;
import com.example.letstalk.Fragments.UsersFragment;
import com.example.letstalk.Models.User;
import com.example.letstalk.Notifications.Client;
import com.google.android.material.tabs.TabLayout;
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

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        // to fetch data from data base
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // fetch data
                /*
                The class attributes must be rhe same names as in database as they are used as keys to retruve the values
                **/
                User user = snapshot.getValue(User.class);

                // display data
                assert user != null;
                userName.setText(user.getUsername());

                //set new image
                if (user.getImageURL().equals("default"))
                    circleImageView.setImageResource(R.mipmap.ic_launcher_round);
                else
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void initialize() {
        circleImageView = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.username_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //get data from data base using uid which is in fire base  user
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        // initialize tool bar
        toolbar = findViewById(R.id.tool_bar_Main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar(), "No tool bar found").setTitle("");

        //initialize tab layout
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.tab_view_id);

        // This second parameter is used to fill only the constructor of view page adapter
        // as the old constructor is deprecated
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPagerAdapter.addFragment(new ChatFragment(), "Chat");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

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
        status("offline");
    }

    //This two functions are used create sub menu in top right of screen

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return false;
    }
}