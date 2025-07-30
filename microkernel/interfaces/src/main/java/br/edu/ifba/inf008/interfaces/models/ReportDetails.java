package br.edu.ifba.inf008.interfaces.models;

import java.time.LocalDate;


public class ReportDetails {
    private final Integer loanId;
    private final String bookTitle;
    private final String bookAuthor;
    private final String clientName;
    private final LocalDate loanDate;

    public ReportDetails(Integer loanId, String bookTitle, String bookAuthor, String clientName, LocalDate loanDate) {
        this.loanId = loanId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.clientName = clientName;
        this.loanDate = loanDate;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    
}

