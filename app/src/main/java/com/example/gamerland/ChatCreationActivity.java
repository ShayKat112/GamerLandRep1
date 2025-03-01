package com.example.gamerland;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatCreationActivity extends AppCompatActivity {

    private Button btnCreateChat;
    private EditText edChatName, edChatDescription;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_creation);
        btnCreateChat = findViewById(R.id.btnCreateChat);
        edChatName = findViewById(R.id.edChatName);
        edChatDescription = findViewById(R.id.edChatDescription);
        db = FirebaseFirestore.getInstance();
        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String chatName = edChatName.getText().toString();
        String chatDescription = edChatDescription.getText().toString();
        if(edChatName.getText().toString().isEmpty() || edChatDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else{
            chatmodel chat = new chatmodel(chatName, chatDescription);
            db.collection("chats").add(chat).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                Toast.makeText(ChatCreationActivity.this, "Chat created", Toast.LENGTH_SHORT).show();
                edChatName.setText("");
                edChatDescription.setText("");
                db.collection("chats").document(documentReference.getId()).update("chatId", documentReference.getId());
            }).addOnFailureListener(e -> {
                Log.w("TAG", "Error adding document", e);
                Toast.makeText(ChatCreationActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                edChatName.setText("");
                edChatDescription.setText("");
            });
        }
    }
}