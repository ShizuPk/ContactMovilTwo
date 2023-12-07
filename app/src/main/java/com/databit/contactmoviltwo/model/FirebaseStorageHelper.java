package com.databit.contactmoviltwo.model;
import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageHelper {

    private StorageReference storageRef;
    private Context context;

    public FirebaseStorageHelper() {
        // Obtén una referencia al Firebase Storage
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    // Método para subir una imagen de perfil al Storage
    public void uploadProfileImage(Uri imageUri, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        if (imageUri != null) {
            // Crea una referencia específica para la imagen del perfil del usuario
            StorageReference profileImageRef = storageRef.child("profile_images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

            // Sube la imagen al Storage
            profileImageRef.putFile(imageUri)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(exception -> {

                        Toast.makeText(context, "Mensaje", Toast.LENGTH_SHORT).show();


                    });
        }
    }
}
