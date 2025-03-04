package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
        Query query = chatsRef.orderBy("name")
                .startAt(queryText)
                .endAt(queryText + "\uf8ff");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(getActivity(), "No chats found", Toast.LENGTH_SHORT).show();
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        String chatName = documentSnapshot.getString("name");
                        if (chatName != null) {
                            createChatButton(chatName);
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

    private void createChatButton(String chatName) {
        // Use requireActivity() to get a non-null Activity reference.
        Button chatButton = new Button(requireActivity());
        chatButton.setText(chatName);
        chatButton.setAllCaps(false);
        // Add the button to your layout container.
        resultContainer.addView(chatButton);
    }
}
