package com.freshism.app.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freshism.app.R;
import com.freshism.app.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DatabaseReference manualRef;
    private DatabaseReference autoRef;
    private DatabaseReference debuRef;
    private DatabaseReference udaraRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    int debu = 0;
    int udara = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi referensi ke Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        debuRef = database.getReference("sensor/debu");
        udaraRef = database.getReference("sensor/udara");
        manualRef = database.getReference("mode/manual/value");
        autoRef = database.getReference("mode/otomatis/value");
        Handler handler = new Handler();

        binding.progressBar2.show();

        // Ngebaca data debu dari Firebase
        debuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ambil nilai debu
                        Double nilaiDebu = dataSnapshot.getValue(Double.class);

                        // Konversi nilaiDebu ke tipe data
                        debu = (int) Math.round(nilaiDebu);

                        // Deklarasi teks dari nilai debu
                        binding.txtDebu.setText(String.valueOf(debu));

                        // Pengecekan hasil status udara & debu
                        String status = getStatus();
                        binding.txtStatus.setText(status);
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Buat pas error
            }
        });

        // Baca data udara dari Firebase
        udaraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Baca nilai udara
                        Double nilaiUdara = dataSnapshot.getValue(Double.class);

                        udara = (int) Math.round(nilaiUdara);

                        // Deklarasi teks dari nilai udara
                        binding.txtUdara.setText(String.valueOf(udara));

                        // Pengecekan hasil status udara & debu
                        String status = getStatus();
                        binding.txtStatus.setText(status);
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Buat pas kesalahan saat membaca data udara
            }
        });

        //Button ke firebase
        // Manual - On
        binding.switchOnManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        // Save nilai boolean "true" ke database, tombol "ON" diklik
                        manualRef.setValue("1");

                        Toast.makeText(getContext(), "Value set to ON", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save nilai boolean "false" ke database, tombol "OFF" diklik
                        manualRef.setValue("0");

                        Toast.makeText(getContext(), "Value set to OFF", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Manual - Off
        binding.switchOffManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        // Save nilai boolean "false" ke database, tombol "OFF" diklik
                        manualRef.setValue("0");

                        Toast.makeText(getContext(), "Value set to OFF", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save nilai boolean "true" ke database, tombol "ON" diklik
                        manualRef.setValue("1");

                        Toast.makeText(getContext(), "Value set to ON", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Otomatis
        binding.switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save nilai boolean "false" ke database, tombol "OFF" diklik
                if (isChecked) {
                    autoRef.setValue(1);
                    Toast.makeText(getContext(), "Value set to ON", Toast.LENGTH_SHORT).show();
                } else {
                    autoRef.setValue(0);
                    Toast.makeText(getContext(), "Value set to OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ambil nilai boolean dari database when aplikasi dimulai
        manualRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Update tampilan tombol pake nilai boolean dari database
                        String value = snapshot.getValue(String.class);
                        if (value != null && value.equals("1")) {
                            // Nilai true (ON), tombol "ON" klik
                            binding.switchOnManual.setChecked(true);
                            binding.switchOffManual.setChecked(false);
                        } else {
                            // Nilai false (OFF), tombol "OFF" klik
                            binding.switchOnManual.setChecked(false);
                            binding.switchOffManual.setChecked(true);
                        }
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Ambil nilai boolean dari database when aplikasi dimulai
        autoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Update tampilan tombol pake nilai boolean dari database
                        Integer value = snapshot.getValue(Integer.class);
                        if (value != null && value.equals(1)) {
                            // Nilai true (ON), tombol "ON" klik
                            binding.switchAuto.setChecked(true);
                        } else {
                            // Nilai false (OFF), tombol "OFF" klik
                            binding.switchAuto.setChecked(false);
                        }
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private String getStatus() {
        String result = "";
        if (udara >= 0 && udara <= 50 && debu >= 0 && debu <= 50) {
            result = "Sehat";
        } else if (udara >= 0 && udara <= 50 && debu >= 51 && debu <= 150) {
            result = "Sehat";
        } else if (udara >= 51 && udara <= 100 && debu >= 0 && debu <= 50) {
            result = "Sehat";
        } else if (udara >= 51 && udara <= 100 && debu >= 51 && debu <= 150) {
            result = "Sehat";
        } else if (udara >= 0 && udara <= 50 && debu >= 151) {
            result = "Tidak Sehat";
        } else if (udara >= 51 && udara <= 100 && debu >= 151) {
            result = "Tidak Sehat";
        } else if (udara >= 100 && debu >= 0 && debu <= 50) {
            result = "Tidak Sehat";
        } else if (udara >= 100 && debu >= 51 && debu <= 150) {
            result = "Tidak Sehat";
        } else if (udara >= 100 && debu >= 151) {
            result = "Tidak Sehat";
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}