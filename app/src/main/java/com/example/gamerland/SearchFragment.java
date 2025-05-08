package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private LinearLayout resultContainer;
    private FirebaseFirestore db;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);

        searchView.setIconifiedByDefault(false);

        resultContainer = view.findViewById(R.id.resultContainer);
        db = FirebaseFirestore.getInstance();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchChats(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchChats(newText);
                return true;
            }
        });
        return view;
    }

    private void searchChats(String queryText) {
        resultContainer.removeAllViews();
        CollectionReference chatsRef = db.collection("chats");
        Query query = db.collection("chats")
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(getActivity(), "No chats found", Toast.LENGTH_SHORT).show();
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        String chatName = documentSnapshot.getString("chatName");
                        String chatId = documentSnapshot.getId();
                        if (chatName != null) {
                            createChatButton(chatId,chatName);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createChatButton(String chatId,String chatName) {
        for (int i = 0; i < resultContainer.getChildCount(); i++) {
            View child = resultContainer.getChildAt(i);
            if (child instanceof Button) {
                Button existingButton = (Button) child;
                if (existingButton.getText().toString().equals(chatName)) {
                    return;
                }
            }
        }
        Button chatButton = new Button(requireActivity());
        chatButton.setText(chatName);
        chatButton.setAllCaps(false);
        chatButton.setPadding(16, 16, 16, 16);
        chatButton.setTextSize(16);
        chatButton.setBackgroundColor(0xFF8BC1EC);
        chatButton.setOnClickListener(v -> {
            resultContainer.removeAllViews();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            ChatFragment chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString("chatId", chatId);
            chatFragment.setArguments(bundle);
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flFragment, chatFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        // Add the button to your layout container.
        resultContainer.addView(chatButton);
    }
}
