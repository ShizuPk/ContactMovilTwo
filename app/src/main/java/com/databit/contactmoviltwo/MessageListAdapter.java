package com.databit.contactmoviltwo;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.model.MessageItem;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.model.MessageItem;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private List<MessageItem> messageItems;

    public MessageListAdapter(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem messageItem = messageItems.get(position);
        holder.bind(messageItem);
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView senderTextView;
        private TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.textViewSender);
            messageTextView = itemView.findViewById(R.id.textViewMessage);
        }

        public void bind(MessageItem messageItem) {
            senderTextView.setText(messageItem.getSender());
            messageTextView.setText(messageItem.getMessage());
        }
    }
}

