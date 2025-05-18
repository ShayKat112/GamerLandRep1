package com.example.gamerland.Adapters;

import android.app.AlertDialog;
import android.util.Log;
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
import com.example.gamerland.models.ReportModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<ReportModel> reportList;

    public ReportAdapter(List<ReportModel> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportModel report = reportList.get(position);
        if (report != null) {
            holder.tvUsernameReported.setText("Username Reported: " + report.getUsernameReported());
            holder.tvReportReason.setText("Report Reason: " + report.getReportReason());
            holder.btnDeleteUser.setOnClickListener(v -> {
                String username = report.getUsernameReported();
                String email = report.getEmailReported();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete user: " + username + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.collection("users")
                                    .document(email) // ← רק אם זה מזהה המסמך
                                    .delete()
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(holder.itemView.getContext(), "User deleted", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(holder.itemView.getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
            holder.btnDismissItem.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();

                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Remove Report")
                        .setMessage("Are you sure you want to delete this report?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // 1. מחיקת המסמך מ־Firestore
                            String reportId = report.getDocumentId();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("reports")
                                    .document(reportId)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        // 2. מחיקה מהרשימה
                                        reportList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                        Toast.makeText(holder.itemView.getContext(), "Report deleted", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(holder.itemView.getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        } else {
            holder.tvUsernameReported.setText("Unknown");
            holder.tvReportReason.setText("Unknown");
        }
    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsernameReported, tvReportReason;
        Button btnDeleteUser;
        ImageButton btnDismissItem;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsernameReported = itemView.findViewById(R.id.tvUsernameReported);
            tvReportReason = itemView.findViewById(R.id.tvReportReason);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
            btnDismissItem = itemView.findViewById(R.id.btnDismissItem);
        }
    }
}
    