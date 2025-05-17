package com.example.gamerland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminChatsControlFragment extends Fragment {

    public AdminChatsControlFragment() {
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
       View view = inflater.inflate(R.layout.fragment_admin_chats_control, container, false);
       return view;
    }
}