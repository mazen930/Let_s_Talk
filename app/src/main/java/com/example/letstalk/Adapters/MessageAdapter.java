package com.example.letstalk.Adapters;

import android.annotation.SuppressLint;
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
import com.example.letstalk.Models.Chat;
import com.example.letstalk.Models.User;
import com.example.letstalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Chat> chatMessagesArrayList;
    private String imageURL;
    FirebaseUser firebaseUser;

    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;

    public MessageAdapter(Context context, ArrayList<Chat> chatMessagesArrayList, String imageURL) {
        this.context = context;
        this.chatMessagesArrayList = chatMessagesArrayList;
        this.imageURL = imageURL;
    }

    //By using function below getViewType() we can check and change layout according to sender and receiver
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_RIGHT)
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = chatMessagesArrayList.get(position);
        holder.message.setText(chat.getMessage());
        if (imageURL.equals("default"))
            holder.senderProfilePicture.setImageResource(R.mipmap.ic_launcher_round);
        else
            Glide.with(context).load(imageURL).into(holder.senderProfilePicture);

        // Check on last message
        if (position == chatMessagesArrayList.size() - 1) {
            holder.text_seen.setVisibility(View.VISIBLE);
            if (chat.isSeen()) {
                holder.text_seen.setText("seen");
            } else
                holder.text_seen.setText("delivered");
        } else {
            holder.text_seen.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return this.chatMessagesArrayList.size();
    }

    // This class used as view holder in recycle view list to preview users
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // these are widget located in left and right chat layout
        // we see that they are both with the same name so that we can change between left and right easily
        TextView message, text_seen;
        ImageView senderProfilePicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.chat_message);
            senderProfilePicture = itemView.findViewById(R.id.image_chatting);
            text_seen = itemView.findViewById(R.id.seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatMessagesArrayList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_RIGHT;
        } else
            return MSG_LEFT;
    }
}
