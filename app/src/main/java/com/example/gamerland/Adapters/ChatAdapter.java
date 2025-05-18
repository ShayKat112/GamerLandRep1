package com.example.gamerland.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerland.R;
import com.example.gamerland.models.messagemodel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<messagemodel> messageList;
    private String chatId;

    public ChatAdapter(List<messagemodel> messageList, String chatId) {
        this.messageList = messageList;
        this.chatId = chatId;
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
        holder.reportButton.setOnClickListener(v -> {
            String messageId = message.getMessageId();
            showReportDialog(holder.itemView.getContext(), chatId, messageId, message);
        });
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
        private ImageButton reportButton;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            senderNameTextView = itemView.findViewById(R.id.tvSenderName);
            avatarImageView = itemView.findViewById(R.id.tvImvAvatar);
            reportButton = itemView.findViewById(R.id.imbtnReportUser);
        }

        public void bind(messagemodel message) {
            messageTextView.setText(message.getText());
            timestampTextView.setText(formatTimestamp(message.getTimestamp()));
            senderNameTextView.setText(message.getSenderUsername());
            avatarImageView.setImageBitmap(changeStringImageToImageView(message.getTvImvAvatar()));
        }

        private Bitmap changeStringImageToImageView(String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        private String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }

    private void showReportDialog(Context context, String chatId, String messageId, messagemodel message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report " + message.getSenderUsername());

        final EditText input = new EditText(context);
        input.setHint("Enter report reason");
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (!reason.isEmpty()) {
                sendReportToMessage(chatId, messageId, message, reason, context);
            } else {
                Toast.makeText(context, "Please enter a reason", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }




    private void sendReportToMessage(String chatId, String messageId, messagemodel message, String reason, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("usernameReported", message.getSenderUsername());
        reportData.put("reportReason", reason);
        reportData.put("messageText", message.getText());
        reportData.put("timestamp", message.getTimestamp());

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .collection("reports")
                .add(reportData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(context, "Report submitted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to submit report", Toast.LENGTH_SHORT).show());
    }



}
