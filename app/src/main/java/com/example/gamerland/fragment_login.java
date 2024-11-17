package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_login extends Fragment implements View.OnClickListener {

    Button btnLogin;
    Button btnRegister;


    public fragment_login() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin=getView().findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            Intent Login = new Intent(fragment_login.this,fragment_register.class);
            startActivity(Login);
        }

    }
}