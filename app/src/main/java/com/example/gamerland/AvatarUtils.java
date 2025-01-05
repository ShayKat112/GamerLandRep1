package com.example.gamerland;

import android.app.AlertDialog;
import android.content.Context;

public class AvatarUtils{

    public static int[] getAvatars() {
        return new int[]{
                R.drawable.avatar1,
                R.drawable.avatar2,
                R.drawable.avatar3,
                R.drawable.avatar4,
                R.drawable.avatar5
        };
    }

    private void showAvatarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }
}

