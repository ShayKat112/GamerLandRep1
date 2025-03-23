package com.example.gamerland;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {
    private TextView tvUsername;
    private ImageView tvImvAvatar;
    private TextView tvAge;
    private TextView tvEmail;
    private TextView tvLikedGames;
    private Button btnChangeProfilePicture;
    private FirebaseFirestore firestore;
    private String email;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(email).get();
        tvUsername.setText("Username: " + username);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}