package com.databit.contactmoviltwo;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactRepository {

    private static final String CONTACTS_NODE = "contacts";
    private DatabaseReference databaseReference;
    private Context context;

    public ContactRepository() {
        // Obtén una referencia a tu base de datos Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void agregarContacto(Usuario usuario) {
        try {
            String nuevoId = databaseReference.child(CONTACTS_NODE).push().getKey();

            // Guarda el objeto Usuario en la base de datos bajo el nodo "contacts"
            databaseReference.child(CONTACTS_NODE).child(nuevoId).setValue(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar el error, por ejemplo, mostrar un mensaje al usuario
            Toast.makeText(context, "Error al agregar contacto", Toast.LENGTH_SHORT).show();
        }
    }
    // Puedes agregar más métodos según tus necesidades (eliminar, actualizar, obtener lista, etc.)
}
