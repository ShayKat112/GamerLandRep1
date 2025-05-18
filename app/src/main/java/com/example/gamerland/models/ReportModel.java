package com.example.gamerland.models;

public class ReportModel {
    private String usernameReported;
    private String reportReason;

    public ReportModel() { } // נדרש על ידי Firestore

    public ReportModel(String usernameReported, String reportReason) {
        this.usernameReported = usernameReported;
        this.reportReason = reportReason;
    }

    public String getUsernameReported() {
        return usernameReported;
    }

    public String getReportReason() {
        return reportReason;
    }
}
