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

import java.util.HashMap;
import java.util.Map;

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
        String chatName = edChatName.getText().toString().trim();
        String chatDescription = edChatDescription.getText().toString().trim();

        if (chatName.isEmpty() || chatDescription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. בונים Map עם כל השדות כולל timestamp
        long now = System.currentTimeMillis();
        Map<String, Object> data = new HashMap<>();
        data.put("chatName", chatName);
        data.put("chatDescription", chatDescription);
        data.put("lastMessageTimestamp", now);

        // 2. מוסיפים למסד
        db.collection("chats")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    String newChatId = documentReference.getId();
                    Log.d("ChatCreation", "Created chat with ID: " + newChatId);

                    // 3. מעדכנים גם את השדה chatId בתוך המסמך
                    Map<String, Object> idUpdate = new HashMap<>();
                    idUpdate.put("chatId", newChatId);
                    documentReference
                            .update(idUpdate)
                            .addOnSuccessListener(aVoid -> {
                                // ניקוי השדות
                                edChatName.setText("");
                                edChatDescription.setText("");
                                Toast.makeText(this, "Chat created successfully", Toast.LENGTH_SHORT).show();

                                // 4. מעבר חזרה ל-HomeActivity
                                Intent i = new Intent(ChatCreationActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w("ChatCreation", "Failed to update chatId", e);
                                // גם כאן אפשר לנווט או להישאר במסך—לפי שיקולך
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w("ChatCreation", "Error adding document", e);
                    Toast.makeText(this, "Error creating chat", Toast.LENGTH_SHORT).show();
                });
    }
}
