package br.edu.ifba.inf008.interfaces;

import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Book;

public interface IBookPlugin extends IPlugin {

    Book addBook(Book book);
    
    boolean deleteBook(Integer bookId);
    
    boolean updateBook(Book book);
    
    Book getBookByID(Integer bookId);
    
    List<Book> getAllBooks();
    
    boolean isBookAvailable(Integer bookId);
    
    boolean decrementAvailableCopies(Integer bookId);
    
    boolean incrementAvailableCopies(Integer bookId);
    
    List<Book> getBooksByAuthor(String author);
    
    List<Book> getBooksByTitle(String title);
}
