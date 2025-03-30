package com.example.gamerland;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBirthDate;
    private ImageView imvAvatar;
    private ImageButton imbtnBack;
    private EditText edUsername, edPassword, edPasswordVerification, edEmail, edLikedGames;
    private Button btnRegister, btnChooseDate, btnChooseAvatar;
    private DatePickerDialog datePickerDialog;
    private static final int GALLERY = 1, CAMERA = 2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI Elements
        tvBirthDate = findViewById(R.id.tvBirthDate);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edPasswordVerification = findViewById(R.id.edPasswordVerification);
        edEmail = findViewById(R.id.edEmail);
        edLikedGames = findViewById(R.id.edLikedGames);
        imbtnBack = findViewById(R.id.imbtnback);
        imvAvatar = findViewById(R.id.imvAvatar);
        btnRegister = findViewById(R.id.btnRegister);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnChooseAvatar = findViewById(R.id.btnChooseAvatar);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Date Picker
        initDatePicker();
        tvBirthDate.setText(getTodayDate());

        // Set Listeners
        btnRegister.setOnClickListener(this);
        btnChooseAvatar.setOnClickListener(this);
        btnChooseDate.setOnClickListener(this);
        imbtnBack.setOnClickListener(this);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            age = Calendar.getInstance().get(Calendar.YEAR) - year;
            month++; // Months are 0-based in DatePicker
            tvBirthDate.setText(makeDateString(day, month, year));

        };

        Calendar cal = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return makeDateString(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            registerUser();
        } else if (view == btnChooseAvatar) {
            showPictureDialog();
        } else if (view == btnChooseDate) { // Add this case
            datePickerDialog.show();
        }
        else if(view == imbtnBack){
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imvAvatar.setImageURI(selectedImageUri);
                }
            } else if (requestCode == CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    imvAvatar.setImageBitmap(photo);
                }
            }
        }
    }



    private void registerUser() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String username = edUsername.getText().toString().trim();
        String birthDate = tvBirthDate.getText().toString();
        String likedGamesStr = edLikedGames.getText().toString();
        List<String> likedGames = likedGamesStr.isEmpty() ? null : Arrays.asList(likedGamesStr.split(","));

        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("RegisterActivity", "User created successfully");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    String profileImage = encodeImageToBase64();
                                    saveUserToFirestore(user.getEmail(), username, birthDate, likedGames, profileImage);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("RegisterActivity", "Sign-up failed: " + task.getException().getMessage());
                    }
                });

    }


    private void saveUserToFirestore(String email, String username, String birthDate, List<String> likedGames, String profileImage) {
        // Create a UserModel object
        usermodel user = new usermodel(email, username, birthDate, likedGames, profileImage, false);
        // Save user to Firestore
        firestore.collection("users").document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "User saved to Firestore", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                }) // Missing closing parenthesis and curly brace here
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }




    private String encodeImageToBase64() {
        Bitmap bitmap = ((BitmapDrawable) imvAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Image Source");
        String[] options = {"Gallery", "Camera"};
        pictureDialog.setItems(options, (dialog, which) -> {
            if (which == 0) {
                choosePhotoFromGallery();
            } else {
                takePhotoFromCamera();
            }
        });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }
    }

}
