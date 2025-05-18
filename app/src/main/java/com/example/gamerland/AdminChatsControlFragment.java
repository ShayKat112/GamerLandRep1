package com.example.gamerland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamerland.Adapters.ReportAdapter;
import com.example.gamerland.Adapters.ReportChatAdapter;
import com.example.gamerland.models.ReportChatModel;
import com.example.gamerland.models.ReportModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminChatsControlFragment extends Fragment {

    TextView tvChatsControl;
    RecyclerView rv;
    FirebaseFirestore db;

    public AdminChatsControlFragment() {
        // Required empty public constructor
    }

    public static AdminChatsControlFragment newInstance(String param1, String param2) {
        AdminChatsControlFragment fragment = new AdminChatsControlFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_chats_control, container, false);
        tvChatsControl = view.findViewById(R.id.tvChatsControl);
        rv = view.findViewById(R.id.rv_admin_chats);
        tvChatsControl.setText("Chats Control");
        db = FirebaseFirestore.getInstance();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ReportChatModel> reports = new ArrayList<>();
        ReportChatAdapter adapter = new ReportChatAdapter(reports);
        rv.setAdapter(adapter);
        Log.d("AdminUsersControlFragment", "onCreateView: called");

// קריאה ל-Firestore
        db.collection("chat_reports")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        ReportChatModel report = doc.toObject(ReportChatModel.class);
                        Log.d("Firestore", "Report loaded: " + report);
                        if (report != null) {
                            reports.add(report);
                        } else {
                            Log.w("Firestore", "Report is null");
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
        Log.d("AdminUsersControlFragment", "onCreateView: adapter notified");
        return view;
    }
}