package br.edu.ifba.inf008.plugins.ui;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifba.inf008.interfaces.IReportPlugin;
import br.edu.ifba.inf008.interfaces.models.ReportDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("unused")
public class ReportViewController implements Initializable{

    private IReportPlugin reportPlugin;

    @FXML private TextField txtSearchId;

    @FXML private TableView<ReportDetails> ReportTable;
    @FXML private TableColumn<ReportDetails, Integer> IdColumn;
    @FXML private TableColumn<ReportDetails, String> ClientColumn;
    @FXML private TableColumn<ReportDetails, String> TitleColumn;
    @FXML private TableColumn<ReportDetails, String> AuthorColumn;
    @FXML private TableColumn<ReportDetails, String> DateColumn;

    private ObservableList<ReportDetails> masterReportList;

    public void setReportPlugin(IReportPlugin plugin) {
        this.reportPlugin = plugin;
        refreshTable();
    }

    private void refreshTable() {
        if (reportPlugin == null) {
            return;
        }
        this.masterReportList = FXCollections.observableArrayList(reportPlugin.generateLoanReport());
        FilteredList<ReportDetails> filteredData = new FilteredList<>(masterReportList, p -> true);        
        ReportTable.setItems(filteredData);
    }

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void SearchById() {
        txtSearchId.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<ReportDetails> filteredList = (FilteredList<ReportDetails>) ReportTable.getItems();

            filteredList.setPredicate(reportDetail -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }                
                String loanIdStr = String.valueOf(reportDetail.getLoanId());
                return loanIdStr.startsWith(newValue);
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        ClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));

        SearchById();
    }
    
}


