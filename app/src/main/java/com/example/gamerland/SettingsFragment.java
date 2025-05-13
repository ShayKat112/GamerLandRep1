package com.example.gamerland;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private TextView tvUsername;
    private ImageView tvImvAvatar;
    private TextView tvAge;
    private TextView tvEmail;
    private TextView tvLikedGames;
    private Button btnChangeProfilePicture, btnSignOut;
    private FirebaseFirestore firestore;
    private static final int GALLERY = 1, CAMERA = 2;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();

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
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getActivity(), WelcomeActivity.class));
            getActivity().finish();
        });
        btnChangeProfilePicture = view.findViewById(R.id.btnChangeProfilePicture);
        btnChangeProfilePicture.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();
        String email = currentUser.getEmail();
        DocumentReference docRef = firestore.collection("users").document(email);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String username = documentSnapshot.getString("username");
                List<String> likedGames = (List<String>) documentSnapshot.get("likedGames");

                String birthDate = documentSnapshot.getString("birthDate");
                String birthDateOnlyYear = birthDate.substring(birthDate.length() - 4);
                int birthDateInteger = Integer.parseInt(birthDateOnlyYear);
                String age = Calendar.getInstance().get(Calendar.YEAR) - birthDateInteger + "";

                tvUsername.setText("Username: " + username);
                tvAge.setText("Age: " + age);
                tvEmail.setText("Email: " + email);
                tvLikedGames.setText("Liked games: " + likedGames);
                saveImageToFirestore(documentSnapshot.getString("profileImage"));
                tvImvAvatar.setImageBitmap(changeStringImageToImageView(documentSnapshot.getString("profileImage")));
            } else {
                Log.d("Firestore", "No such document");
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching document", e));
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnChangeProfilePicture) {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
            pictureDialog.setTitle("Select Image Source");
            String[] options = {"Gallery", "Camera"};
            pictureDialog.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY);
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                    }
                }
            });
            pictureDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null && data.getData() != null) {
                    tvImvAvatar.setImageURI(data.getData());

                    // עדכון למסד הנתונים
                    String encodedImage = encodeImageToBase64();
                    saveImageToFirestore(encodedImage);
                }
            } else if (requestCode == CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    tvImvAvatar.setImageBitmap(photo);

                    // עדכון למסד הנתונים
                    String encodedImage = encodeImageToBase64();
                    saveImageToFirestore(encodedImage);
                }
            }
        }
    }

    private Bitmap changeStringImageToImageView(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void saveImageToFirestore(String encodedImage) {
        firestore.collection("users").document(currentUser.getEmail()).update("profileImage", encodedImage);
    }

    private String encodeImageToBase64() {
        Bitmap bitmap = ((BitmapDrawable) tvImvAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }
}
