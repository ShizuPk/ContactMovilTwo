package com.databit.contactmoviltwo.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.databit.contactmoviltwo.HomeActivity;
import com.databit.contactmoviltwo.MainActivity;
import com.databit.contactmoviltwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Inicializar EditTexts
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Inicializar botones y TextView
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Configurar el OnClickListener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClick();
            }
        });

        // Configurar el OnClickListener para el TextView de registro
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegistration();
            }
        });
    }

    private void onLoginButtonClick() {
        // Obtener el nombre de usuario y la contraseña ingresados
        String nombreUsuario = usernameEditText.getText().toString().trim();
        String contraseña = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(contraseña)) {
            // Mostrar un mensaje de error si el nombre de usuario o la contraseña están vacíos
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(nombreUsuario, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Redirigir a la actividad Home
                            goToHomeActivity();
                        } else {
                            // Si falla, mostrar un mensaje al usuario.
                            if (task.getException() != null) {
                                if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(Login.this, "Usuario no registrado. Por favor, regístrate primero.", Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Login.this, "Credenciales incorrectas. Por favor, verifica tus datos.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Fallo de inicio de sesión. Por favor, inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
                                }

                                // Registrar detalles del error en el Log
                                Log.e("Login", "Fallo de inicio de sesión", task.getException());
                            }
                        }
                    }
                });
    }

    private void goToHomeActivity() {
        // Crear un Intent para ir a la Activity Home (ajusta el nombre de la clase según tu implementación)
        Intent intent = new Intent(Login.this, HomeActivity.class);
        startActivity(intent);
        // Finalizar esta actividad para que el usuario no pueda volver atrás con el botón de retroceso
        finish();
    }

    private void goToRegistration() {
        // Crear un Intent para ir a la Activity de registro (ajusta el nombre de la clase según tu implementación)
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}
