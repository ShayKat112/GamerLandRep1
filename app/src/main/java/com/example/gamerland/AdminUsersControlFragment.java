package com.example.gamerland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return view;
    }
}