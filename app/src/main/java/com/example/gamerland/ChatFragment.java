package com.example.gamerland;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<messagemodel> messageList;
    private FirebaseFirestore db;
    private TextView chatName;
    private TextView chatDescription;
    private String chatId;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        chatName = view.findViewById(R.id.tvChatName);
        chatDescription = view.findViewById(R.id.tvChatDescription);
        db = FirebaseFirestore.getInstance();

        // קבלת ה-Chat ID מהארגומנטים שהועברו ל-Fragment
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
        } else {
            Toast.makeText(getActivity(), "Error: No chatId found!", Toast.LENGTH_SHORT).show();
            return view;
        }
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
        loadMessages();
        sendButton.setOnClickListener(v -> sendMessage());
        db.collection("chats").document(chatId).get().addOnSuccessListener(documentSnapshot -> {
            Log.d("ChatFragment", "Chat name: " + documentSnapshot.getString("chatName"));
            chatName.setText(documentSnapshot.getString("chatName").toString());
            chatDescription.setText(documentSnapshot.getString("chatDescription").toString());
        });
        return view;
    }

    private void loadMessages() {
        db.collection("chats").document(chatId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("ChatFragment", "Firestore error", error);
                        return;
                    }
                    if (value != null) {
                        messageList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            messagemodel message = doc.toObject(messagemodel.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        String email = currentUser.getEmail();
        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            String senderUsername = documentSnapshot.getString("username");
            String profileImage = documentSnapshot.getString("profileImage");
            if (text.isEmpty()) return;
            long now = System.currentTimeMillis();
            Log.d("ChatFragment", "Sending message: " + senderUsername);
            messagemodel message = new messagemodel(text, senderUsername, profileImage, now);
            // 1. שולחים את ההודעה
            db.collection("chats")
                    .document(chatId)
                    .collection("messages")
                    .add(message)
                    .addOnSuccessListener(documentReference -> {
                        // מנקים את השדה
                        messageInput.setText("");
                        // 2. מעדכנים את זמן ההודעה האחרון בשדה בקובץ ה־chat עצמו
                        db.collection("chats")
                                .document(chatId)
                                .update("lastMessageTimestamp", now);
                    })
                    .addOnFailureListener(e -> Log.e("ChatFragment", "Error sending message", e));
        });
    }
}
