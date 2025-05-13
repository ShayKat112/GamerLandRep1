package com.example.gamerland;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<messagemodel> messageList;

    public ChatAdapter(List<messagemodel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        messagemodel message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private TextView timestampTextView;
        private TextView senderNameTextView;
        private ImageView avatarImageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            senderNameTextView = itemView.findViewById(R.id.tvSenderName);
            avatarImageView = itemView.findViewById(R.id.tvImvAvatar);
        }

        public void bind(messagemodel message) {
            messageTextView.setText(message.getText());
            timestampTextView.setText(formatTimestamp(message.getTimestamp()));
            senderNameTextView.setText(message.getSenderUsername());
            avatarImageView.setImageBitmap(changeStringImageToImageView(message.getTvImvAvatar()));
        }

        private Bitmap changeStringImageToImageView(String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }

        private String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}
