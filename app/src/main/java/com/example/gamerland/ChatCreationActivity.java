package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ChatCreationActivity extends AppCompatActivity {

    private Button btnCreateChat;
    private EditText edChatName, edChatDescription;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_creation);

        // אתחול View וה־Firestore
        btnCreateChat = findViewById(R.id.btnCreateChat);
        edChatName = findViewById(R.id.edChatName);
        edChatDescription = findViewById(R.id.edChatDescription);
        db = FirebaseFirestore.getInstance();

        btnCreateChat.setOnClickListener(v -> createChat());
    }

    private void createChat() {
        chatmodel chat = new chatmodel();
        chat.setChatName(edChatName.getText().toString().trim());
        chat.setChatDescription(edChatDescription.getText().toString().trim());
        chat.setChatId(edChatName.getText().toString().trim());
        Log.d("ChatCreationActivity", "Chat ID: " + chat.getChatId());

        if (chat.getChatName().isEmpty() || chat.getChatDescription().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("chats").document(edChatName.getText().toString().trim()).set(chat)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Chat created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChatCreationActivity.this, HomeActivity.class));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ChatCreationActivity", "Error creating chat: " + e.getMessage());
                        Toast.makeText(this, "Error creating chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }
}
