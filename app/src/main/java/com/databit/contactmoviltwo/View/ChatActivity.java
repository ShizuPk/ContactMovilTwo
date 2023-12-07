package com.databit.contactmoviltwo.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.databit.contactmoviltwo.MessageListAdapter;
import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.model.MessageItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Button buttonSendMessage;
    private EditText editTextMessage;
    private FirebaseUser currentUser;

    private ArrayList<MessageItem> messageList;
    private MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("messages");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Inicializar la lista y el adaptador
        messageList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messageList);

        // Configurar RecyclerView para mostrar la lista de mensajes
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageListAdapter);

        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        editTextMessage = findViewById(R.id.editTextMessage);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el texto del mensaje desde el EditText
                String messageText = editTextMessage.getText().toString();

                // Verificar si el usuario está autenticado
                if (currentUser != null) {
                    // Obtener el nombre de usuario asociado con el usuario actual
                    ObtenerNombreUsuarioYEnviarMensaje(messageText);
                    editTextMessage.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Obtener referencia a la base de datos de Firebase
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        // Agregar un listener para escuchar cambios en los mensajes
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Obtener el mensaje y agregarlo a la lista
                MessageItem messageItem = snapshot.getValue(MessageItem.class);
                messageList.add(messageItem);

                // Notificar al adaptador que los datos han cambiado
                messageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Implementación vacía
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Implementación vacía
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Implementación vacía
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Implementación vacía
            }
        });
    }

    private void sendMessageToFirebase(String messageText, String sender) {
        // Obtener una clave única para el mensaje
        String messageId = databaseReference.push().getKey();
        if (currentUser != null) {
            // Crear un objeto MessageItem con el remitente y el mensaje
            MessageItem messageItem = new MessageItem(sender, messageText);

            // Guardar el mensaje en Firebase usando la clave única
            databaseReference.child(messageId).setValue(messageItem);
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obtener el nombre de usuario asociado con el usuario actual y enviar el mensaje
    private void ObtenerNombreUsuarioYEnviarMensaje(final String messageText) {
        // Verificar si el usuario actual no es nulo
        if (currentUser != null) {
            // Obtener el ID del usuario actual
            String userId = currentUser.getUid();

            // Obtener una referencia a la base de datos de usuarios
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // Obtener el nombre de usuario del usuario actual desde la base de datos
            usersRef.child(userId).child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Verificar si el dataSnapshot es válido y contiene datos
                    if (dataSnapshot.exists()) {
                        // Obtener el valor del campo "firstName" desde la base de datos
                        String nombreUsuario = dataSnapshot.getValue(String.class);

                        // Llamar a la función para enviar el mensaje a Firebase
                        sendMessageToFirebase(messageText, nombreUsuario);  // <-- Aquí se envía el nombre real del usuario
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error si la lectura de datos se cancela
                    Toast.makeText(ChatActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
