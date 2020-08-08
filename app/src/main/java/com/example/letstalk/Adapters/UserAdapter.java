package com.example.letstalk.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letstalk.Models.User;
import com.example.letstalk.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> userArrayList;

    public UserAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentUser = this.userArrayList.get(position);
        holder.username.setText(currentUser.getUsername());
        if (currentUser.getImageURL().equals("default")) {
            holder.profilePicture.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(context).load(currentUser.getImageURL()).into(holder.profilePicture);
        }
    }


    @Override
    public int getItemCount() {
        return this.userArrayList.size();
    }

    // This class used as view holder in recycle view list to preview users
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_row);
            profilePicture = itemView.findViewById(R.id.profile_picture_row);
        }
    }
}
