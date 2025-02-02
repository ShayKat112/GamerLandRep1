package com.example.gamerland;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBirthDate;
    private ImageView imvAvatar;
    private ImageButton imbtnBack;
    private EditText edUsername, edPassword, edPasswordVerification, edEmail, edLikedGames;
    private Button btnRegister, btnChooseDate, btnChooseAvatar;
    private String emailAdress, password;
    private DatePickerDialog datePickerDialog;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    final int REQUEST_CODE_GALLERY = 999;



    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvBirthDate.setText(getTodayDate());
        initDatePicker();
        imbtnBack = findViewById(R.id.imbtnback);
        imbtnBack.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edPasswordVerification = findViewById(R.id.edPasswordVerification);
        edEmail = findViewById(R.id.edEmail);
        edLikedGames = findViewById(R.id.edLikedGames);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnChooseAvatar = findViewById(R.id.btnChooseAvatar);
        imvAvatar = findViewById(R.id.imvAvatar);


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(this);
        btnChooseAvatar.setOnClickListener(this);
        btnChooseDate.setOnClickListener(this);
        imbtnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month + 1, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                tvBirthDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }



    @Override
    public void onClick(View view) {

        if (view == btnRegister) {
            emailAdress = edEmail.getText().toString().trim();
            password = edPassword.getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(emailAdress, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Intent btnRegisterClicked = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(btnRegisterClicked);
            String likedGamesInput = edLikedGames.getText().toString();
            List<String> likedGames = Arrays.asList(likedGamesInput.split(","));
            createUser(edEmail.getText().toString(), edUsername.getText().toString(), likedGames);

        }

        if (view == btnChooseAvatar) {
            showPictureDialog();
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Please choose where the picture is going to be taken from:");
        String[] pictureDialogsItems = {
                "From Gallery", "From Camera"
        };
        pictureDialog.setItems(pictureDialogsItems, (dialog, which) -> {
            if (which == 0) {
                choosePhotoFromGallary();
            } else if (which == 1) {
                takePhotoFromCamera();
            }
        });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == this.RESULT_CANCELED){
            return;
        }
        if(requestCode == GALLERY){
                if(data != null){
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                        imvAvatar.setImageBitmap(bitmap);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        if(requestCode == CAMERA){
            Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
            imvAvatar.setImageBitmap(thumbnail);
            Toast.makeText(RegisterActivity.this,"Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public String saveImage(Bitmap myBitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirecotery = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if(!wallpaperDirecotery.exists()){
            wallpaperDirecotery.mkdirs();
        }
        try{
            File f = new File(wallpaperDirecotery, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1){
            e1.printStackTrace();
        }
        return "";
    }

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

    public void createUser(String email, String username, List<String> likedGames) {
        User user = new User(email, username, likedGames);

        firestore.collection("users")
                .document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User successfully written!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }





    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
