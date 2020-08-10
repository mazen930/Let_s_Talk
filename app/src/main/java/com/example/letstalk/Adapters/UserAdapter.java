package com.example.letstalk.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letstalk.MessageActivity;
import com.example.letstalk.Models.User;
import com.example.letstalk.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> userArrayList;
    boolean isChatting;

    public UserAdapter(Context context, ArrayList<User> userArrayList, boolean isChatting) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.isChatting = isChatting;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User currentUser = this.userArrayList.get(position);
        holder.username.setText(currentUser.getUsername());
        if (currentUser.getImageURL().equals("default")) {
            holder.profilePicture.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(context).load(currentUser.getImageURL()).into(holder.profilePicture);
        }

        // We used item view instead of profile picture or name to start chat with ony one
        // we can either tab on profile picture or name to start chat as long as we don't
        // assign click listener for any of them
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Care here we take context which was passed from currently working activity
                // so we can move with intent
                Intent intent = new Intent(context, MessageActivity.class);

                // get the id of the user we want to talk to
                intent.putExtra("userId", currentUser.getId());
                context.startActivity(intent);
            }
        });

        if (this.isChatting) {
            if (currentUser.getStatus().equals("online")) {
                holder.statusOnline.setVisibility(View.VISIBLE);
                holder.statusOffline.setVisibility(View.GONE);
            } else {
                holder.statusOnline.setVisibility(View.GONE);
                holder.statusOffline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.statusOnline.setVisibility(View.GONE);
            holder.statusOffline.setVisibility(View.GONE);

        }
    }


    @Override
    public int getItemCount() {
        return this.userArrayList.size();
    }

    // This class used as view holder in recycle view list to preview users
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePicture, statusOnline, statusOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_row);
            profilePicture = itemView.findViewById(R.id.profile_picture_row);
            statusOffline = itemView.findViewById(R.id.img_offline);
            statusOnline = itemView.findViewById(R.id.img_online);
        }
    }
}
