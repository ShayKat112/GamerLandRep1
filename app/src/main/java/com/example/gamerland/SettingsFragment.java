package com.example.gamerland;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(email);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String username = documentSnapshot.getString("username");
                String age = documentSnapshot.getString("age");
                String email = documentSnapshot.getString("email");
                String likedGames = documentSnapshot.getString("likedGames");

                tvUsername.setText("Username: " + username);
                tvAge.setText("Age: " + age);
                tvEmail.setText("Email: " + email);
                tvLikedGames.setText("Liked games: " + likedGames);

        }
    }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching document", e));
    return view;
}
}
