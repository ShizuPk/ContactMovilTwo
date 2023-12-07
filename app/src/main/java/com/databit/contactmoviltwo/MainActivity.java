package com.databit.contactmoviltwo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.databit.contactmoviltwo.View.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//...

public class MainActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, usernameEditText, emailEditText, passwordEditText;
    private Button btnregistro, btniniciarsesion;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Inicializar EditTexts
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Inicializar botones
        btnregistro = findViewById(R.id.btnregistro);
        btniniciarsesion = findViewById(R.id.btninicarsesion);

        // Configurar el OnClickListener para el botón de registro
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClick();
            }
        });

        // Configurar el OnClickListener para el botón de inicio de sesión
        btniniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }



    private void onRegisterButtonClick() {
        // Obtener valores de los campos de texto
        String nombre = firstNameEditText.getText().toString().trim();
        String apellido = lastNameEditText.getText().toString().trim();
        String nombreUsuario = usernameEditText.getText().toString().trim();
        String correo = emailEditText.getText().toString().trim();
        String contrasena = passwordEditText.getText().toString();

        // Validar campos
        if (camposRegistroSonValidos(nombre, apellido, nombreUsuario, correo, contrasena)) {
            // Crear instancia de UsuarioModel
            com.databit.contactmoviltwo.Modelo.UsuarioModel nuevoUsuario = new com.databit.contactmoviltwo.Modelo.UsuarioModel(nombre, apellido, correo, nombreUsuario);

            // Registrar nuevo usuario en Firebase Authentication
            mAuth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Guardar datos del usuario en la base de datos (Realtime Database)
                                DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuario");
                                usuarioRef.child(user.getUid()).setValue(nuevoUsuario);


                            }
                        } else {
                            // Manejar errores durante el registro
                            Toast.makeText(MainActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean camposRegistroSonValidos(String nombre, String apellido, String correo, String nombreUsuario, String contrasena) {
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }





}
