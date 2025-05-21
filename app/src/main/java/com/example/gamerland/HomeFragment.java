package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.gamerland.Adapters.ChatListAdapter;
import com.example.gamerland.models.chatmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private RecyclerView rv;
    private ChatListAdapter adapter;
    private List<chatmodel> chatList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnCreateChat, btnAdminLogin;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private boolean isPlaying = false;
    private ImageButton btnMusicControl;

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
        btnAdminLogin = view.findViewById(R.id.btnAdminLogin);
        btnAdminLogin.setVisibility(View.GONE);
        btnMusicControl = view.findViewById(R.id.btnMusicControl);
        String email = currentUser.getEmail();

        btnMusicControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent musicIntent = new Intent(getActivity(), MusicService.class);

                if (isPlaying) {
                    getActivity().stopService(musicIntent);
                    btnMusicControl.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop_music));
                } else {
                    getActivity().startService(musicIntent);
                    btnMusicControl.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_music));
                }

                isPlaying = !isPlaying;
            }
        });

        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean isAdmin = documentSnapshot.getBoolean("admin");
                        Log.d("HomeFragment", "isAdmin: " + isAdmin);
                        if (isAdmin) {
                            btnAdminLogin.setVisibility(View.VISIBLE);
                        }
                    }
                });
        btnAdminLogin.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), AdminActivity.class);
                        startActivity(intent);
        });

        adapter = new ChatListAdapter(chatList, chat -> {
            Bundle b = new Bundle();
            ChatFragment chatFragment = new ChatFragment();
            b.putString("chatId", chat.getChatId());
            chatFragment.setArguments(b);
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });

        rv.setAdapter(adapter);
        loadChats();

        btnCreateChat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatCreationActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadChats() {
        db.collection("chats")
                .orderBy("chatId", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    chatList.clear();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        chatmodel chat1 = doc.toObject(chatmodel.class);
                        chatList.add(chat1);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HomeFragment", "Error loading chats", e));
    }
}
