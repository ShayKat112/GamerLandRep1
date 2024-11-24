package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_register extends AppCompatActivity implements View.OnClickListener {
    ImageButton imbtnBack;
    EditText UserName,Password,passwordVer,birthdate,email,likedGame;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imbtnBack = findViewById(R.id.imbtnBack);
        imbtnBack.setOnClickListener(this);
        register = findViewById(R.id.btnRegister);
        UserName = findViewById(R.id.edUsername);
        Password = findViewById(R.id.edPassword);
        passwordVer = findViewById(R.id.edPasswordVerification);
        birthdate = findViewById(R.id.edBirthDate);
        email = findViewById(R.id.edEmail);
        likedGame = findViewById(R.id.edLikedGames);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username;
                Username = UserName.getText().toString();


            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent imbtnBackClicked = new Intent(activity_register.this, MainActivity.class);
        startActivity(imbtnBackClicked);
    }
}