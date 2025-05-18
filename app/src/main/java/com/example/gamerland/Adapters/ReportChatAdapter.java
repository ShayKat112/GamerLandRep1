package com.example.gamerland.Adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerland.R;
import com.example.gamerland.models.ReportChatModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReportChatAdapter extends RecyclerView.Adapter<ReportChatAdapter.ReportViewHolder> {

    private List<ReportChatModel> reportList;

    public ReportChatAdapter(List<ReportChatModel> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_chats, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportChatModel report = reportList.get(position);
        if (report != null) {
            holder.tvChatReported.setText("Chat Reported: " + report.getChatReported());
            holder.tvReportReason.setText("Report Reason: " + report.getReportReason());
            holder.btnDeleteChat.setOnClickListener(v -> {
                String chatId = report.getChatId(); // ודא שהשדה קיים במודל!
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Chat")
                        .setMessage("Are you sure you want to delete chat ID: " + chatId + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.collection("chats")
                                    .document(chatId)
                                    .delete()
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(holder.itemView.getContext(), "Chat deleted", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(holder.itemView.getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
            holder.btnDismissItem.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                reportList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            });

        } else {
            holder.tvChatReported.setText("Unknown");
            holder.tvReportReason.setText("Unknown");
        }
    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatReported, tvReportReason;
        Button btnDeleteChat;
        ImageButton btnDismissItem;



        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatReported = itemView.findViewById(R.id.tvChatReported);
            tvReportReason = itemView.findViewById(R.id.tvReportReason);
            btnDeleteChat = itemView.findViewById(R.id.btnDeleteChat);
            btnDismissItem = itemView.findViewById(R.id.btnDismissItem);
        }
    }
}
