package com.example.gamerland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamerland.R;
import com.example.gamerland.Chat;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    private List<Chat> chats;
    private OnChatClickListener listener;

    public ChatListAdapter(List<Chat> chats, OnChatClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_button, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.btn.setText(chat.getChatName());
        holder.btn.setOnClickListener(v -> listener.onChatClick(chat));
    }

    @Override
    public int getItemCount() { return chats.size(); }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_chat);
        }
    }
}
