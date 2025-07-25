package br.edu.ifba.inf008.plugins;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.interfaces.utils.DBConnector;



public class BookManager {
    
    public Book addBook(Book book){
        String sql = "INSERT INTO books (title, author, isbn, published_year, copies_available) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                pstmt.setInt(4, book.getPublishedYear());
                pstmt.setInt(5, book.getCopiesAvailable());
                int affectedRows = pstmt.executeUpdate();
                if(affectedRows > 0){
                    try(ResultSet rs = pstmt.getGeneratedKeys()){
                        if(rs.next()){
                            book.setBookId(rs.getInt(1));
                        }
                    }
                }
                return book;
        }catch (SQLException e) {
            System.err.println("Error while trying to add the new book: " + e.getMessage());
            return null;

        }
    }

    public boolean updateBook(Book book){
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_year = ?, copies_available = ? WHERE book_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                pstmt.setInt(4, book.getPublishedYear());
                pstmt.setInt(5, book.getCopiesAvailable());
                pstmt.setInt(6, book.getBookId());
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error while trying to update the book: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBook(Integer bookID){
        String sql = "DELETE FROM books WHERE book_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, bookID);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }catch (SQLException e){
            System.err.println("Error while trying to delete the book: " + e.getMessage());
            return false;
        }
    }

    public Book getBookByID(Integer bookID){
        String sql = "SELECT book_id, title, author, isbn, published_year, copies_available FROM books WHERE book_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, bookID);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublishedYear(rs.getInt("published_year"));
                    book.setCopiesAvailable(rs.getInt("copies_available"));
                    return book;
                }
            }            
        }catch (SQLException e) {
            System.err.println("Error while trying to get the book by ID given: " + e.getMessage());
        }
        return null;        
    }

    public Book getBookByIsbn(String isbn){
        String sql = "SELECT book_id, title, author, isbn, published_year, copies_available FROM books WHERE isbn = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, isbn);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublishedYear(rs.getInt("published_year"));
                    book.setCopiesAvailable(rs.getInt("copies_available"));
                    return book;
                }                
            }            
        }catch (SQLException e) {
            System.err.println("Error while trying to get the book by ISBN: " + e.getMessage());
        }
        return null;
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, isbn, published_year, copies_available FROM books";
        try(Connection connection = DBConnector.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublishedYear(rs.getInt("published_year"));
                    book.setCopiesAvailable(rs.getInt("copies_available"));
                    books.add(book);
                }

        }catch (SQLException e) {
            System.err.println("Error while trying to retrieve all books: " + e.getMessage());
        }
        return books;
    }

    public boolean isBookAvailable(Integer bookId){
        String sql = "SELECT copies_available FROM books WHERE book_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, bookId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("copies_available") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while trying to check if the book is available: " + e.getMessage());
        }
        return false;
    }

    public boolean decrementAvailableCopies(Integer bookID){
        String sql = "UPDATE books SET copies_available = copies_available -1 WHERE book_id = ? AND copies_available >0";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, bookID);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }catch (SQLException e) {
            System.err.println("Error while trying to decrement the available copies: " + e.getMessage());
            return false;
        }
    }
    
    public boolean incrementAvailableCopies(Integer bookID){
        String sql = "UPDATE books SET copies_available = copies_available + 1 WHERE book_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, bookID);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }catch (SQLException e) {
            System.err.println("Error while trying to decrement the available copies: " + e.getMessage());
            return false;
        }
    }

}