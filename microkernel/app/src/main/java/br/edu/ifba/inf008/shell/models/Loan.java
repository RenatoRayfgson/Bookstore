package br.edu.ifba.inf008.shell.models;
import java.time.LocalDate;

import br.edu.ifba.inf008.shell.models.Book.LoanStatus;

public class Loan {
    private Integer loanId;
    private Integer userId;
    private Integer bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;

    private LocalDate dueDate;
    private LoanStatus status;
    
    public Loan(Integer loanId, Integer userId, Integer bookID, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, LoanStatus status) {
        this.loanId = loanId;
        this.userId = userId;
        this.bookId = bookID;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Loan() {
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
