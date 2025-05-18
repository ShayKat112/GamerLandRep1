package com.example.gamerland.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReportModel {
    private String usernameReported;
    private String emailReported;
    private String reportReason;

    // חובה: בונה ריק עבור Firestore
    public ReportModel() {}

    // אופציונלי: בונה מלא לשימוש פנימי
    public ReportModel(String usernameReported, String reportReason) {
        this.usernameReported = usernameReported;
        this.reportReason = reportReason;
    }

    // Getters
    public String getUsernameReported() {
        return usernameReported;
    }

    public String getReportReason() {
        return reportReason;
    }

    // 🔧 Setters – חובה כדי ש־Firestore יוכל למלא את הערכים
    public void setUsernameReported(String usernameReported) {
        this.usernameReported = usernameReported;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public String getEmailReported() {
        return emailReported;
    }

    public void setEmailReported(String emailReported) {
        this.emailReported = emailReported;
    }
}
