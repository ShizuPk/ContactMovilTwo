package com.databit.contactmoviltwo.View;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.databit.contactmoviltwo.Contact;
import com.databit.contactmoviltwo.ContactAdapter;
import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class AddContactActivity extends AppCompatActivity {

    private EditText editTextFriendEmail, editTextContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editTextFriendEmail = findViewById(R.id.editTextFriendEmail);
        editTextContactName = findViewById(R.id.editTextContactName);

        Button buttonAddContact = findViewById(R.id.buttonAddContact);
        buttonAddContact.setOnClickListener(view -> agregarContacto(view));

    }

    public void agregarContacto(View view) {
        String friendEmail = editTextFriendEmail.getText().toString().trim();
        String contactName = editTextContactName.getText().toString().trim();

        if (!TextUtils.isEmpty(friendEmail) && !TextUtils.isEmpty(contactName)) {
            // Aquí puedes agregar la lógica para guardar el contacto en la base de datos
            // Puedes utilizar Firebase o cualquier otro mecanismo de almacenamiento

            // Crear un nuevo objeto Contact con la información ingresada
            Contact newContact = new Contact();
            newContact.setCorreo(friendEmail);
            newContact.setNombre(contactName);

            // Guardar el nuevo contacto en la base de datos (usando Firebase como ejemplo)
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
            userRef.push().setValue(newContact);

            // Devolver el resultado a ContactListActivity
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);

            // Cerrar la actividad
            finish();
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

}

