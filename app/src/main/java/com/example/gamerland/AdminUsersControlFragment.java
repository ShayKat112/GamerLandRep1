package com.example.gamerland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamerland.Adapters.ReportAdapter;
import com.example.gamerland.models.ReportModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersControlFragment extends Fragment {

    TextView tvUsersControl;
    RecyclerView rv;

    public AdminUsersControlFragment() {
        // Required empty public constructor
    }

    public static AdminUsersControlFragment newInstance(String param1, String param2) {
        AdminUsersControlFragment fragment = new AdminUsersControlFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_users_control, container, false);
        tvUsersControl = view.findViewById(R.id.tvUsersControl);
        rv = view.findViewById(R.id.rv_admin_users);
        tvUsersControl.setText("Users Control");
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<ReportModel> reports = new ArrayList<>();
        ReportAdapter adapter = new ReportAdapter(reports);
        rv.setAdapter(adapter);

// קריאה ל-Firestore
        String collectionPath = "chats/chatId/messages/messageId/reports"; // או "chat_reports"
        db.collection(collectionPath)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        ReportModel report = doc.toObject(ReportModel.class);
                        reports.add(report);
                    }
                    adapter.notifyDataSetChanged();
                });
        return view;
    }
}