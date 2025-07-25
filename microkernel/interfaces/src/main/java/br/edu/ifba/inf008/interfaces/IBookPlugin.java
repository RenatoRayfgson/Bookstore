package br.edu.ifba.inf008.interfaces;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Book;

public interface IBookPlugin extends IPlugin {
    
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(Integer id);
    
    List<Book> getAllBooks();
    Book getBookByIsbn(String isbn);
    Book getBookByID(Integer id);
    
    boolean isBookAvailable(Integer bookId);
    boolean incrementAvailableCopies(Integer bookId);
    boolean decrementAvailableCopies(Integer bookId);

}
