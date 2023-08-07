package com.freshism.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freshism.app.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        Bundle savedInstanceState = null;
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        binding.loginButton2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                if (email.isEmpty()) {
                    binding.email.setError("Harus diisi");
                } else if (password.isEmpty()) {
                    binding.password.setError("Harus diisi");
                } else {
                    // Pengecekan Autentikasi pada Firebase Auth menggunakan email dan password
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login berhasil

                                        // Set
                                        PreferencesController.setDataLogin(getApplicationContext(), email);

                                        // Pindah ke halaman dashboard
                                        Intent intent = new Intent(Login.this, Dashboard.class);
                                        startActivity(intent);
                                    } else {
                                        // Login gagal
                                        Toast.makeText(Login.this, "Login gagal. Periksa kembali email dan kata sandi Anda.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }));
    }}




