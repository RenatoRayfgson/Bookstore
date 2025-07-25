package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.edu.ifba.inf008.interfaces.IBookPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.plugins.ui.BookViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
public class BookPlugin implements IBookPlugin {
    
    private final BookManager bookManager = new BookManager();

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();
        
        MenuItem menuItem = uiController.createMenuItem("Book", "Book Management");
        
        menuItem.setOnAction((ActionEvent e) -> {
            try {                
                String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/BookView.fxml";
                URL fxmlUrl = getClass().getResource(fxmlPath);                
                
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(fxmlUrl);
                fxmlLoader.setClassLoader(getClass().getClassLoader());
                
                Parent bookUi = fxmlLoader.load();
                
                
                BookViewController controller = fxmlLoader.getController();                
                
                controller.setPlugin(this);
                
                uiController.setMainContent(bookUi);

            } catch (IOException ex) {
                System.err.println("Falha ao carregar a UI do plugin de livro.");
                ex.printStackTrace();
            }
        });

        return true;
    }    

    @Override
    public boolean addBook(Book book) {        
        return bookManager.addBook(book) != null;
    }

    @Override
    public boolean updateBook(Book book) {
        return bookManager.updateBook(book);
    }

    @Override
    public boolean deleteBook(Integer bookId) {
        return bookManager.deleteBook(bookId);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookManager.getBookByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookManager.getAllBooks();
    }

    @Override
    public boolean isBookAvailable(Integer bookId) {
        return bookManager.isBookAvailable(bookId);
    }

    @Override
    public boolean incrementAvailableCopies(Integer bookId) {
        return bookManager.incrementAvailableCopies(bookId);
    }

    @Override
    public boolean decrementAvailableCopies(Integer bookId) {
        return bookManager.decrementAvailableCopies(bookId);
    }

    @Override
    public Book getBookByID(Integer id) {
        return bookManager.getBookByID(id);
    }
}