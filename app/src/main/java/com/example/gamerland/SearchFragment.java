package com.example.gamerland;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
    }
}