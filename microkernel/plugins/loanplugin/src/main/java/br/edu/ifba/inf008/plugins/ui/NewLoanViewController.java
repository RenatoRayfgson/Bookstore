package br.edu.ifba.inf008.plugins.ui;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

import br.edu.ifba.inf008.interfaces.IBookPlugin;
import br.edu.ifba.inf008.interfaces.ILoanPlugin;
import br.edu.ifba.inf008.interfaces.IUserPlugin;
import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.interfaces.models.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("unused")
public class NewLoanViewController implements Initializable {

    private ILoanPlugin loanPlugin;
    private IBookPlugin bookPlugin;
    private IUserPlugin userPlugin;

    @FXML private TextField txtName;
    @FXML private TextField txtTitle;
    
    @FXML private Button RegisterButton;

    @FXML private TableView<User> UsersTable;
    @FXML private TableColumn<User, Integer> UserIdColumn;
    @FXML private TableColumn<User, String> NameColumn;

    @FXML private TableView<Book> BooksTable;
    @FXML private TableColumn<Book, Integer> BookIdColumn;
    @FXML private TableColumn<Book, String> TitleColumn;
    @FXML private TableColumn<Book, Integer> ACColumn;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        this.userPlugin = ServiceLoader.load(IUserPlugin.class, classLoader).findFirst().orElse(null);
        this.bookPlugin = ServiceLoader.load(IBookPlugin.class, classLoader).findFirst().orElse(null);
        
        if(userPlugin == null || bookPlugin == null) {
            displayAlert("Error", "Required plugins not found.", Alert.AlertType.ERROR);
            return;
        }
        
        UserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        BookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ACColumn.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));
        
        UsersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFieldsAndCheckReadiness());
        BooksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFieldsAndCheckReadiness());
        
        txtName.setEditable(false);
        txtTitle.setEditable(false);
        RegisterButton.setDisable(true);
    }
    
    private void refreshTables() {
        UsersTable.setItems(FXCollections.observableArrayList(userPlugin.getAllUsers()));
        BooksTable.setItems(FXCollections.observableArrayList(bookPlugin.getAllBooks()));
    }

    private void fillFieldsAndCheckReadiness() {
        User selectedUser = UsersTable.getSelectionModel().getSelectedItem();
        Book selectedBook = BooksTable.getSelectionModel().getSelectedItem();

        txtName.setText(selectedUser != null ? selectedUser.getName() : "");
        txtTitle.setText(selectedBook != null ? selectedBook.getTitle() : "");       
        
        boolean isReady = selectedUser != null && selectedBook != null && selectedBook.getCopiesAvailable() > 0;
        RegisterButton.setDisable(!isReady);
    }
    
    private void clearData() {
        UsersTable.getSelectionModel().clearSelection();
        BooksTable.getSelectionModel().clearSelection();
        txtName.clear();
        txtTitle.clear();
        RegisterButton.setDisable(true);
    }

    private void displayAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setPlugin(ILoanPlugin plugin) {
        this.loanPlugin = plugin;
        refreshTables();
    }

    @FXML
    private void RegisterButtonAction(ActionEvent event) {
        User selectedUser = UsersTable.getSelectionModel().getSelectedItem();
        Book selectedBook = BooksTable.getSelectionModel().getSelectedItem();

        if(selectedUser == null || selectedBook == null) {
            displayAlert("Error", "Please select a user and a book.", Alert.AlertType.ERROR);
            return;
        }

        if(selectedBook.getCopiesAvailable() <= 0) {
            displayAlert("Error", "No copies available for this book.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate dueDate = LocalDate.now().plusDays(14);
        var newLoan = loanPlugin.registerLoan(selectedUser.getUserId(), selectedBook.getBookId(), dueDate);
        if (newLoan != null) {
            displayAlert("Success", "Loan registered successfully!", Alert.AlertType.INFORMATION);
            refreshTables();
            clearData();
        } else {
            displayAlert("Error", "Failed to register loan.", Alert.AlertType.ERROR);
        }
    }

}
