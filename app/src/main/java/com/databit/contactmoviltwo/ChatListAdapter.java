package com.databit.contactmoviltwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.model.ChatListItem;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatListItem> chatListItems;

    public ChatListAdapter() {
        this.chatListItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatListItem chatListItem = chatListItems.get(position);
        holder.bind(chatListItem);
    }

    @Override
    public int getItemCount() {
        return chatListItems.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        private TextView senderTextView;
        private TextView lastMessageTextView;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.textViewSender);
            lastMessageTextView = itemView.findViewById(R.id.textViewLastMessage);
        }

        public void bind(ChatListItem chatListItem) {
            senderTextView.setText(chatListItem.getSender());
            lastMessageTextView.setText(chatListItem.getLastMessage());
        }
    }
}
