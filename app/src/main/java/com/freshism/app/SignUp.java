package com.freshism.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freshism.app.databinding.ActivitySignupBinding;
import com.freshism.app.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.mail.MessagingException;

public class SignUp extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String recipientEmail = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmpassword.getText().toString();

//        Validasi Form Registrasi
        if (recipientEmail.isEmpty()) {
            binding.email.setError("Harus diisi");
        } else if (password.isEmpty()) {
            binding.password.setError("Harus diisi");
        } else if (password.length() < 6) {
            binding.password.setError("Minimal 6 karakter");
        } else if (confirmPassword.isEmpty()) {
            binding.confirmpassword.setError("Harus diisi");
        } else if (!confirmPassword.equals(password)) {
            binding.confirmpassword.setError("Harus sama dengan password");
        } else {
            // Melakukan proses pendaftaran pengguna
            fAuth.createUserWithEmailAndPassword(recipientEmail, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "Berhasil daftar", Toast.LENGTH_SHORT).show();
                            String uid = user.getUid();

                            // Menyimpan data pendaftaran ke Firebase Database
                            DatabaseReference userRef = fDatabase.getReference("users").child(uid);
                            userRef.child("email").setValue(recipientEmail);
                            userRef.child("password").setValue(password);

                            // konfirmasi email jika pendaftaran berhasil
                            String subject = "Konfirmasi Pendaftaran";
                            String body = "Terima kasih telah mendaftar. Silakan klik tautan berikut untuk mengkonfirmasi pendaftaran.";

                            /*try {
                                EmailSignupSender.sendEmail(recipientEmail, subject, body);
                                Toast.makeText(SignUp.this, "Email konfirmasi telah dikirim", Toast.LENGTH_SHORT).show();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                                Toast.makeText(SignUp.this, "Gagal mengirim email konfirmasi", Toast.LENGTH_SHORT).show();
                            }*/

                            finish();
                        }
                    });
        }
    }
}
