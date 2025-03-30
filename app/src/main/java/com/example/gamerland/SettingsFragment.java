package com.example.gamerland;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class SettingsFragment extends Fragment {
    private TextView tvUsername;
    private ImageView tvImvAvatar;
    private TextView tvAge;
    private TextView tvEmail;
    private TextView tvLikedGames;
    private Button btnChangeProfilePicture;
    private FirebaseFirestore firestore;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();


    public SettingsFragment() {
    }

    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvImvAvatar = view.findViewById(R.id.tvImvAvatar);
        tvAge = view.findViewById(R.id.tvAge);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvLikedGames = view.findViewById(R.id.tvLikedGames);
        btnChangeProfilePicture = view.findViewById(R.id.btnChangeProfilePicture);

        firestore = FirebaseFirestore.getInstance();
        String email = currentUser.getEmail();
                DocumentReference docRef = firestore.collection("users").document(email);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String profileImage = documentSnapshot.getString("profileImage");
                    Bitmap profileBitmap = decodeBase64ToBitmap(profileImage);
                    List<String> likedGames = (List<String>) documentSnapshot.get("likedGames");

                    String birthDate = documentSnapshot.getString("birthDate");
                    String birthDateOnlyYear = birthDate.substring(birthDate.length() - 4);
                    int birthDateInteger = Integer.parseInt(birthDateOnlyYear);
                    String age = Calendar.getInstance().get(Calendar.YEAR) - birthDateInteger + "";

                    tvUsername.setText("Username: " + username);
                    tvAge.setText("Age: " + age);
                    tvEmail.setText("Email: " + email);
                    tvLikedGames.setText("Liked games: " + likedGames);
                    tvImvAvatar.setImageBitmap(profileBitmap);
                } else {
                    Log.d("Firestore", "No such document");
                }
            }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching document", e));
    return view;
}
}
