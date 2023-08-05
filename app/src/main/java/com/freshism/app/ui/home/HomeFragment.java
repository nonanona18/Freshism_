package com.freshism.app.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freshism.app.PreferencesController;
import com.freshism.app.R;
import com.freshism.app.databinding.FragmentHomeBinding;
import com.freshism.app.ui.dashboard.DashboardFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    int debu = 0;
    int udara = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = PreferencesController.getEmail(getContext());
        binding.useremail.setText(email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference debuRef = database.getReference("sensor/debu");
        DatabaseReference udaraRef = database.getReference("sensor/udara");
        Handler handler = new Handler();

        String statusInfo = getString(R.string.status_info_bar);
        binding.txtExplaination.setText(statusInfo);

        // Ambil data dari Firebase Realtime Database realtime
        debuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ambil nilai dari dataSnapshot ke TextView
                        debu = (int) Math.round(dataSnapshot.getValue(Double.class));

                        // Ambil status
                        String status = getStatus().first;
                        // Ambil deskripsi hasil
                        String deskripsi = getStatus().second;

                        binding.txtExplaination.setText(deskripsi);
                        binding.btnStatus.setText(status);
                    }
                }, 500);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle jika ada error
            }
        });

        // Ambil data dari Firebase Realtime Database realtime
        udaraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ambil nilai dari dataSnapshot ke TextView
                        udara = (int) Math.round(dataSnapshot.getValue(Double.class));

                        // Ambil status
                        String status = getStatus().first;
                        // Ambil deskripsi hasil
                        String deskripsi = getStatus().second;

                        binding.txtExplaination.setText(deskripsi);
                        binding.btnStatus.setText(status);
                    }
                }, 500);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle jika ada error
            }
        });

        binding.btnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.containerCustomer, new DashboardFragment())
                        .commit();
            }
        });
    }

    private Pair<String, String> getStatus() {
        String result = "";
        String deskripsi = "";
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
        return new Pair<>(result, deskripsi);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}