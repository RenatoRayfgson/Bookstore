package br.edu.ifba.inf008.plugins.ui;

import br.edu.ifba.inf008.interfaces.IBookPlugin;
import br.edu.ifba.inf008.interfaces.models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("unused")
public class BookViewController {

    private IBookPlugin bookPlugin;
    
    @FXML private TextField txtTitle;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtPY; // Publication Year
    @FXML private TextField txtAC; // Available Copies
    
    @FXML private Button CreateButton;
    @FXML private Button UpdateButton;
    @FXML private Button DeleteButton;
    
    @FXML private TableView<Book> BooksTable;
    @FXML private TableColumn<Book, Integer> IdColumn;
    @FXML private TableColumn<Book, String> TitleColumn;
    @FXML private TableColumn<Book, String> AuthorColumn;
    @FXML private TableColumn<Book, Integer> AvailableColumn;

    private ObservableList<Book> booksList;

    private void refreshTable() {
        booksList = FXCollections.observableArrayList(bookPlugin.getAllBooks());
        BooksTable.setItems(booksList);
    }

    private void clearData() {        
        txtTitle.clear();
        txtAuthor.clear();
        txtIsbn.clear();
        txtPY.clear();
        txtAC.clear();
        BooksTable.getSelectionModel().clearSelection();
    }

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fillFieldsWithSelectedBook(Book book) {
        if (book != null) {            
            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtIsbn.setText(book.getIsbn());
            txtPY.setText(String.valueOf(book.getPublishedYear()));
            txtAC.setText(String.valueOf(book.getCopiesAvailable()));
        }
    }

    public void setPlugin(IBookPlugin plugin) {
        this.bookPlugin = plugin;
        refreshTable();
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }        
        return str.matches("\\d+");
    }

    @FXML
    public void initialize() {
        
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        AvailableColumn.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));

        // Adiciona "ouvinte" para cliques na tabela
        BooksTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> fillFieldsWithSelectedBook(newValue)
        );
    }

    @FXML
    public void CreateButtonAction(ActionEvent event){
        if(txtTitle.getText().isEmpty() || txtAuthor.getText().isEmpty() || txtIsbn.getText().isEmpty() ||
            txtPY.getText().isEmpty() || txtAC.getText().isEmpty()){
                displayAlert("Input Error", "All fields must be filled.");
                return;
        }
        if(!isNumeric(txtPY.getText()) || !isNumeric(txtAC.getText()) || !isNumeric(txtIsbn.getText())) {
            displayAlert("Input Error", "Please enter valid numbers for year, ISBN and copies.");
            return;
        }        
        try {
            Book newBook = new Book();
            newBook.setTitle(txtTitle.getText());
            newBook.setAuthor(txtAuthor.getText());
            newBook.setIsbn(txtIsbn.getText());
            newBook.setPublishedYear(Integer.parseInt(txtPY.getText()));
            newBook.setCopiesAvailable(Integer.parseInt(txtAC.getText()));

            if(bookPlugin.addBook(newBook)) {
                refreshTable();
                clearData();
            } else {
                displayAlert("Error", "Something wen wrong.");
            }
        } catch (NumberFormatException e) {
            displayAlert("Input Error", "The numbers you entered are kind weird.");
        }
    }

    @FXML
    public void UpdateButtonAction(ActionEvent event) {
        Book selectedBook = BooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            displayAlert("Selection Error", "Please select a book to update.");
            return;
        }
        if(!isNumeric(txtPY.getText()) || !isNumeric(txtAC.getText()) || !isNumeric(txtIsbn.getText())) {
            displayAlert("Input Error", "Please enter valid numbers for year, ISBN and copies.");
            return;
        }
        try {
            selectedBook.setTitle(txtTitle.getText());
            selectedBook.setAuthor(txtAuthor.getText());
            selectedBook.setIsbn(txtIsbn.getText());
            selectedBook.setPublishedYear(Integer.parseInt(txtPY.getText()));
            selectedBook.setCopiesAvailable(Integer.parseInt(txtAC.getText()));

            if(bookPlugin.updateBook(selectedBook)) {
                refreshTable();
                clearData();
            } else {
                displayAlert("Error", "Something went wrong.");
            }
        } catch (NumberFormatException e) {
            displayAlert("Input Error", "The numbers you entered are kind weird.");        
        }
    }

    @FXML
    public void DeleteButtonAction(ActionEvent event) {
        Book selectedBook = BooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            displayAlert("Selection Error", "Please select a book to delete.");
            return;
        }
        if (bookPlugin.deleteBook(selectedBook.getBookId())) {
            refreshTable();
            clearData();
        } else {
            displayAlert("Error", "Something went wrong.");
        }
    }

}
