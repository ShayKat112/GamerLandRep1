package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_register extends AppCompatActivity implements View.OnClickListener {
    ImageButton imbtnBack;
    EditText UserName,PasswordInput,passwordVer,birthdate,email,likedGame;
    Button register;
    String emailAdress, password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        imbtnBack = findViewById(R.id.imbtnBack);
        imbtnBack.setOnClickListener(this);
        register = findViewById(R.id.btnRegister);
        UserName = findViewById(R.id.edUsername);
        PasswordInput = findViewById(R.id.edPassword);
        passwordVer = findViewById(R.id.edPasswordVerification);
        birthdate = findViewById(R.id.edBirthDate);
        email = findViewById(R.id.edEmail);
        likedGame = findViewById(R.id.edLikedGames);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if(view == imbtnBack){
        Intent imbtnBackClicked = new Intent(activity_register.this, activity_welcome.class);
        startActivity(imbtnBackClicked);
        }


        if (view == register) {
            emailAdress = email.getText().toString().trim();
            password = PasswordInput.getText().toString().trim();
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
        }
    }
}