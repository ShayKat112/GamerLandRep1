package com.example.gamerland;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
    private EditText edUsername,edPassword,edPasswordVerification,edEmail,edLikedGames;
    private Button btnRegister, btnChooseDate, btnChooseAvatar;
    private String emailAdress, password;
    private DatePickerDialog datePickerDialog;


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

        int[] avatars = {
                R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
                R.drawable.avatar4, R.drawable.avatar5
        };

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(day, month + 1, year);
    }

    private void initDatePicker(){
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

        showAvatarSelectionDialog(avatars);

        if(view == imbtnBack){
        Intent imbtnBackClicked = new Intent(activity_register.this, activity_welcome.class);
        startActivity(imbtnBackClicked);
        }


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

        if (view == btnChooseAvatar){

        }
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void showAvatarSelectionDialog(int[] avatars) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_register.this);
        builder.setTitle("Choose Your Avatar");

        // RecyclerView setup
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_avatar_selection, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this, RecyclerView.HORIZONTAL, false));

        AvatarAdapter adapter = new AvatarAdapter(activity_register.this, avatars, new AvatarAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(int avatarResId) {
                imvAvatar.setImageResource(avatarResId);
                Toast.makeText(activity_register.this, "Avatar selected!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        recyclerView.setAdapter(adapter);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}