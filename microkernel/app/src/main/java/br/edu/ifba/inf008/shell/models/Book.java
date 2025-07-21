package br.edu.ifba.inf008.shell.models;
import java.time.LocalDate;

public class Book {
    private Integer bookId;
    private String isbn;
    private String title;
    private String author;
    private int publishedYear;
    private int copiesAvailable;
    private LocalDate dueDate;
    private LoanStatus status;

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

    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        OVERDUE
    }

    public Book(Integer bookId, String isbn, String title, String author, int publishedYear, int copiesAvailable, LocalDate dueDate, LoanStatus status) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.copiesAvailable = copiesAvailable;
        this.dueDate = dueDate;
        this.status = status;
    }       
    
    public Book() {
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }
}
