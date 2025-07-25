package br.edu.ifba.inf008.interfaces;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Book;

public interface IBookPlugin {

    Book getBookById(String id);
    List<Book> getAllBooks();
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(String id);
    Book getBookByIsbn(String isbn);
    Book getBookByID(Integer id);
    boolean isBookAvailable(Integer bookId);
    boolean incrementAvailableCopies(Integer bookId);
    boolean decrementAvailableCopies(Integer bookId);

}
