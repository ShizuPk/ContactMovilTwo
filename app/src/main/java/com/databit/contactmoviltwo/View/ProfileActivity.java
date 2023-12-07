package com.databit.contactmoviltwo.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.databit.contactmoviltwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private TextView usernameTextView;
    private Button updateProfileButton, changePhotoButton;
    private ImageView profileImageView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;  // Almacena la URI de la imagen seleccionada

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Inicializar vistas
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameTextView = findViewById(R.id.usernameTextView);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        profileImageView = findViewById(R.id.profileImageView);
        loadProfileImage();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // El nodo del usuario existe, obtén el nombre de usuario
                    String username = snapshot.child("username").getValue(String.class);

                    // Establece el nombre de usuario en el TextView
                    usernameTextView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error si ocurre
                Toast.makeText(ProfileActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
            }
        });


        // Configurar el Listener para el botón de cambiar foto
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Configurar el Listener para el botón de actualizar perfil
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para actualizar el perfil
                updateProfile();
            }
        });
        String currentUsername = obtenerNombreDeUsuario(); // Implementa esta función según tu lógica
        usernameTextView.setText(currentUsername);

        String currentmail = obtenerCorreoElectronico();
        emailEditText.setText(currentmail);
    }

    private String obtenerNombreDeUsuario() {
        // Obtener la instancia actual del usuario de Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verificar si el usuario está autenticado
        if (currentUser != null) {
            // El usuario está autenticado, puedes acceder a su nombre de usuario o nombre aquí
            String nombreDeUsuario = currentUser.getDisplayName();

            // Verificar si el nombre de usuario está disponible
            if (nombreDeUsuario != null && !nombreDeUsuario.isEmpty()) {
                return nombreDeUsuario;
            } else {
                // Si no hay un nombre de usuario, puedes devolver otro valor o manejarlo según tus necesidades
                return "Nombre de usuario no disponible";
            }
        } else {
            // Si el usuario no está autenticado, puedes manejarlo según tus necesidades
            return "Usuario no autenticado";
        }
    }
    private String obtenerCorreoElectronico() {
        // Obtener la instancia actual del usuario de Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verificar si el usuario está autenticado
        if (currentUser != null) {
            // El usuario está autenticado, puedes acceder a su correo electrónico aquí
            String correoElectronico = currentUser.getEmail();

            // Verificar si el correo electrónico está disponible
            if (correoElectronico != null && !correoElectronico.isEmpty()) {
                return correoElectronico;
            } else {
                // Si no hay un correo electrónico, puedes devolver otro valor o manejarlo según tus necesidades
                return "Correo electrónico no disponible";
            }
        } else {
            // Si el usuario no está autenticado, puedes manejarlo según tus necesidades
            return "Usuario no autenticado";
        }
    }
    private void loadProfileImage() {
        // Obtener la referencia al nodo del usuario actual en la base de datos
        DatabaseReference userRef = databaseReference.child("users").child(currentUser.getUid());

        // Obtener la URL de la imagen de perfil desde la base de datos
        userRef.child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.getValue(String.class);

                    // Cargar la imagen utilizando Glide o la biblioteca de tu elección
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this).load(imageUrl).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error si ocurre
                Toast.makeText(ProfileActivity.this, "Error al obtener la imagen de perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void updateProfile() {
        // Obtén los nuevos datos del perfil
        String newFirstName = firstNameEditText.getText().toString().trim();
        String newLastName = lastNameEditText.getText().toString().trim();

        // Actualiza los datos en la base de datos de Firebase
        updateUserData(newFirstName, newLastName);

        // Si se seleccionó una nueva imagen, sube la imagen al Storage y actualiza la URL en la base de datos
        if (imageUri != null) {
            uploadProfileImage(imageUri);
        } else {
            // Si no se seleccionó una nueva imagen, solo muestra un mensaje de éxito
            Toast.makeText(this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserData(String newFirstName, String newLastName) {
        // Obtiene la referencia al nodo del usuario actual en la base de datos
        DatabaseReference userRef = databaseReference.child("users").child(currentUser.getUid());

        // Actualiza los datos del usuario
        userRef.child("firstName").setValue(newFirstName);
        userRef.child("lastName").setValue(newLastName);
    }

    private void uploadProfileImage(Uri imageUri) {
        // Obtén una referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Crea una referencia específica para la imagen del perfil del usuario
        StorageReference profileImageRef = storageRef.child("profile_images/" + currentUser.getUid() + ".jpg");

        // Sube la imagen al Storage
        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // La imagen se cargó exitosamente
                    // Ahora, obtén la URL de descarga de la imagen y actualiza la base de datos
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // La URL de descarga está disponible en el objeto 'uri'
                        // Actualiza la URL de la imagen en la base de datos
                        String imageUrl = uri.toString();
                        DatabaseReference userRef = databaseReference.child("users").child(currentUser.getUid());
                        userRef.child("profileImageUrl").setValue(imageUrl);

                        // Muestra un mensaje de éxito
                        Toast.makeText(this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Maneja errores durante la carga de la imagen
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                });
    }
}
