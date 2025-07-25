package br.edu.ifba.inf008.plugins.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import br.edu.ifba.inf008.interfaces.IUserPlugin;
import br.edu.ifba.inf008.interfaces.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserViewController implements Initializable{
    
    private IUserPlugin userPlugin;
    
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;

    
    @FXML private Button CreateButton;
    @FXML private Button UpdateButton;
    @FXML private Button DeleteButton;

    @FXML private TableView<User> UsersTable;
    @FXML private TableColumn<User, Integer> IdColumn;
    @FXML private TableColumn<User, String> NameColumn;
    @FXML private TableColumn<User, String> EmailColumn;

    private ObservableList<User> userList;

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fillFieldsWithUser(User user) {
        if (user != null) {            
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
        }
    }

    private void refreshTable() {        
        userList = FXCollections.observableArrayList(userPlugin.getAllUsers());
        UsersTable.setItems(userList);
    }

    private void cleanData() {    
    txtName.clear();
    txtEmail.clear();
    UsersTable.getSelectionModel().clearSelection();
    }

    private boolean isValidEmail(String email) {
    
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        UsersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFieldsWithUser(newValue));
        
    }

    public void setUserPlugin(IUserPlugin plugin) {
        this.userPlugin = plugin;
        refreshTable();
    }

    @FXML
    private void CreateButtonAction(ActionEvent event){
        if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty()){
            displayAlert("Error", "All fields must be filled.");
            return;
        }
        String email = txtEmail.getText();
        if (!isValidEmail(email)) {
            displayAlert("Invalid Email", "Please enter a valid email format (e.g., name@domain.com).");
            return;
        }
        User newUser = new User(txtName.getText(), txtEmail.getText());
        userPlugin.addUser(newUser);
        refreshTable();
        cleanData();
    }

    @FXML
    private void UpdateButtonAction(ActionEvent event){
        if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty()){
            displayAlert("Error", "All fields must be filled.");
            return;
        }
        String email = txtEmail.getText();
        if (!isValidEmail(email)) {
            displayAlert("Invalid Email", "Please enter a valid email format (e.g., name@domain.com).");
            return;
        }
        User updatedUser = new User(txtName.getText(), txtEmail.getText());
        updatedUser.setUserId(UsersTable.getSelectionModel().getSelectedItem().getUserId());
        userPlugin.updateUser(updatedUser);
        refreshTable();
        cleanData();
    }

    @FXML
    private void DeleteButtonAction(ActionEvent event){        
        userPlugin.deleteUser(UsersTable.getSelectionModel().getSelectedItem().getUserId());
        refreshTable();
        cleanData();
    }
}


