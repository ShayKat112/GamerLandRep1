package com.example.gamerland;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class activity_register extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBirthDate;
    private ImageView imvAvatar;
    private ImageButton imbtnBack;
    private EditText edUsername, edPassword, edPasswordVerification, edEmail, edLikedGames;
    private Button btnRegister, btnChooseDate, btnChooseAvatar;
    private String emailAdress, password;
    private DatePickerDialog datePickerDialog;
    

    int[] avatars = AvatarUtils.getAvatars();


    private FirebaseAuth mAuth;

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

        btnRegister.setOnClickListener(this);

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
                                Toast.makeText(activity_register.this, "Authentication success.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity_register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Intent btnRegisterClicked = new Intent(activity_register.this, activity_home.class);
            startActivity(btnRegisterClicked);
        }

        if (view == btnChooseAvatar) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_avatar_selection);
            dialog.show();
        }
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
