package com.example.gamerland;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String chatId;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
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
        if (text.isEmpty()) return;

        messagemodel message = new messagemodel(text, "user_id", System.currentTimeMillis());
        db.collection("chats").document(chatId).collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> messageInput.setText(""))
                .addOnFailureListener(e -> Log.e("ChatFragment", "Error sending message", e));
    }
}
