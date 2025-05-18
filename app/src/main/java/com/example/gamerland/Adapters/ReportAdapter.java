package com.example.gamerland.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerland.R;
import com.example.gamerland.models.ReportModel;

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
        holder.tvUsernameReported.setText("Username Reported: \"" + report.getUsernameReported() + "\"");
        holder.tvReportReason.setText("Report Reason: \"" + report.getReportReason() + "\"");
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsernameReported, tvReportReason;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsernameReported = itemView.findViewById(R.id.tvUsernameReported);
            tvReportReason = itemView.findViewById(R.id.tvReportReason);
        }
    }
}
    