package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUsername;
    EditText edPassword;
    Button btnLogin;
    Button btnRegister;
    Button btnAdminLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            edUsername = findViewById(R.id.edUsername);
            edPassword = findViewById(R.id.edPassword);
            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);
            btnAdminLogin = findViewById(R.id.btnAdminLogin);

    };

    @Override
    public void onClick(View view) {
        Intent buttonClicked = new Intent(MainActivity.this, activity_register.class);
        startActivity(buttonClicked);
    }
}
