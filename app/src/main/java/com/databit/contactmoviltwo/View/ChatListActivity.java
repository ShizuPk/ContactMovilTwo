package com.databit.contactmoviltwo.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.databit.contactmoviltwo.ChatListAdapter;
import com.databit.contactmoviltwo.R;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Configurar RecyclerView para mostrar la lista de conversaciones
        RecyclerView recyclerView = findViewById(R.id.recyclerViewChatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Configurar el adaptador para la lista de conversaciones
        ChatListAdapter chatListAdapter = new ChatListAdapter();
        recyclerView.setAdapter(chatListAdapter);
    }
}
