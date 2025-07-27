package br.edu.ifba.inf008.plugins.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.edu.ifba.inf008.interfaces.ILoanPlugin;
import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.interfaces.models.Book.LoanStatus;
import br.edu.ifba.inf008.interfaces.models.Loan;
import br.edu.ifba.inf008.interfaces.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
public class ReturnLoanViewController implements Initializable {    

    public class LoanDetail{
        private final Integer loanId;
        private final String clientName;
        private final String bookTitle;
        private final LoanStatus status;

        public LoanDetail(Integer loanId, String clientName, String bookTitle, LoanStatus status) {
            this.loanId = loanId;
            this.clientName = clientName;
            this.bookTitle = bookTitle;
            this.status = status;
        }

        public Integer getLoanId() {
            return loanId;
        }
        public String getClientName() {
            return clientName;
        }
        public String getBookTitle() {
            return bookTitle;
        }
        public LoanStatus getStatus() {
            return status;
        }

    }

    private ILoanPlugin loanPlugin; 

    @FXML private TextField txtLoanID;
    @FXML private TextField txtTitle;
    @FXML private TextField txtClient;
    @FXML private TextField SearchLoan;

    @FXML private Button FinishButton;

    @FXML private TableView<LoanDetail> LoanTable;
    @FXML private TableColumn<LoanDetail, Integer> IdColumn;
    @FXML private TableColumn<LoanDetail, String> TitleColumn;
    @FXML private TableColumn<LoanDetail, String> ClientColumn;
    @FXML private TableColumn<LoanDetail, LoanStatus> StatusColumn;

    private ObservableList<LoanDetail> masterLoanDetailList;

    private void refreshTable() {
        if (loanPlugin == null) return;        
        
        List<Loan> activeLoans = loanPlugin.getActiveLoans();        
        List<User> allUsers = loanPlugin.getAllUsers();
        List<Book> allBooks = loanPlugin.getAllBooks();        
        
        List<LoanDetail> loanDetails = new ArrayList<>();
        for (Loan loan : activeLoans) {            
            String clientName = allUsers.stream()
                .filter(user -> user.getUserId().equals(loan.getUserId()))
                .map(User::getName)
                .findFirst()
                .orElse("Unknown User");            
            
            String bookTitle = allBooks.stream()
                .filter(book -> book.getBookId().equals(loan.getBookId()))
                .map(Book::getTitle)
                .findFirst()
                .orElse("Unknown Book");

            loanDetails.add(new LoanDetail(loan.getLoanId(), clientName, bookTitle, loan.getStatus()));
        }
        
        this.masterLoanDetailList = FXCollections.observableArrayList(loanDetails);
        FilteredList<LoanDetail> filteredLoans = new FilteredList<>(masterLoanDetailList, p -> true);
        LoanTable.setItems(filteredLoans);
        
        clearData();
    }
    
    private void clearData() {
        LoanTable.getSelectionModel().clearSelection();
        txtLoanID.clear();
        txtTitle.clear();
        txtClient.clear();
        FinishButton.setDisable(true);
    }

    private void fillFieldsWithSelectedLoan(LoanDetail loanDetail) {
        if (loanDetail != null) {
            txtLoanID.setText(String.valueOf(loanDetail.getLoanId()));
            txtTitle.setText(loanDetail.getBookTitle());
            txtClient.setText(loanDetail.getClientName());
            FinishButton.setDisable(false);
        } else {
            clearData();
        }
    }
    
    private void displayAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPlugin(ILoanPlugin loanPlugin){
        this.loanPlugin = loanPlugin; 
        refreshTable();        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {        

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        ClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        LoanTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFieldsWithSelectedLoan(newValue));

        SearchLoan.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<LoanDetail> filteredList = (FilteredList<LoanDetail>) LoanTable.getItems();
            
            filteredList.setPredicate(loanDetail -> {                
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }                
                String lowerCaseFilter = newValue.toLowerCase();                
                
                String loanIdStr = String.valueOf(loanDetail.getLoanId());                
                
                return loanIdStr.startsWith(lowerCaseFilter);
            });
        });

        txtLoanID.setEditable(false);
        txtTitle.setEditable(false);
        txtClient.setEditable(false);
        FinishButton.setDisable(true); 
    }
    
    @FXML
    private void FinishButtonAction(ActionEvent event){
        LoanDetail selectedLoan = LoanTable.getSelectionModel().getSelectedItem();
        
        if (selectedLoan == null) {
            displayAlert("Error", "Please select a loan to return.", Alert.AlertType.ERROR);
            return;
        }

        var returnedLoan = loanPlugin.registerReturn(selectedLoan.getLoanId());
        
        if(returnedLoan != null){
            displayAlert("Success", "Loan returned successfully.", Alert.AlertType.INFORMATION);
            refreshTable();
            clearData();
        } else {
            displayAlert("Error", "Failed to return the loan. Please try again.", Alert.AlertType.ERROR);
        }
        
    }

}
