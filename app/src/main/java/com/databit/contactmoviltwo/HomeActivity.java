package com.databit.contactmoviltwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.databit.contactmoviltwo.View.ChatActivity;
import com.databit.contactmoviltwo.View.Login;
import com.databit.contactmoviltwo.View.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private ImageView btnuser;
    private  Button btnChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnuser= findViewById(R.id.btnuser);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama al método que maneja la acción de hacer clic en el botón de chat
                irAChatActivity();
            }
        });

        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        if (mAuth.getCurrentUser() == null) {
            // Si el usuario no está autenticado, redirigir a MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    // Esta función se llama cuando se presiona el botón en el archivo XML
    public void irAContactListActivity(View view) {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }
    public void irAChatActivity() {
        // Aquí debes poner el código para ir a la actividad de chat
        // Por ejemplo, puedes usar un Intent para iniciar la nueva actividad
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}