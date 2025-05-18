package com.example.gamerland.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReportChatModel {
    private String chatReported;
    private String chatId; // ← הוספנו שדה זה
    private String reportReason;
    private String documentId;

    public ReportChatModel() {}

    public String getChatReported() {
        return chatReported;
    }

    public void setChatReported(String chatReported) {
        this.chatReported = chatReported;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
