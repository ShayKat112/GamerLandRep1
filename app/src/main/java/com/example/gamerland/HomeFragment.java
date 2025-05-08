package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rv;
    private ChatListAdapter adapter;
    private List<Chat> chatList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnCreateChat;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        rv = view.findViewById(R.id.rv_home_chats);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        btnCreateChat = view.findViewById(R.id.btnCreateChat);
        adapter = new ChatListAdapter(chatList, chat -> {
            Bundle b = new Bundle();
            b.putString("chatId", chat.getChatId());
            Intent i = new Intent(getContext(), ChatFragment.class);
            i.putExtra("chatId", chat.getChatId());
            startActivity(i);
        });
        rv.setAdapter(adapter);
        db.collection("chats")
                .orderBy("chatId", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    chatList.clear();
                    // לולאה עם טיפוס מפורש במקום var
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        Chat chat = doc.toObject(Chat.class);
                        chatList.add(chat);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("HomeFragment", "Error loading chats", e)
                );



        btnCreateChat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatCreationActivity.class);
            startActivity(intent);
        });
        return view;
    }
}