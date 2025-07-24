package br.edu.ifba.inf008.shell;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

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

    @FXML private TextField txtId;
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
            txtId.setText(String.valueOf(user.getUserId()));
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
        }
    }

    private void refreshTable() {        
        userList = FXCollections.observableArrayList(userPlugin.getAllUsers());
        UsersTable.setItems(userList);
    }

    private void cleanData() {
    txtId.clear();
    txtName.clear();
    txtEmail.clear();
    UsersTable.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userPlugin = ServiceLoader.load(IUserPlugin.class).findFirst().orElse(null);
        if(this.userPlugin == null){
            displayAlert("Error", "User plugin not found.");
            return;
        }
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        UsersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFieldsWithUser(newValue));

        refreshTable();
    }

    @FXML
    private void CreateButtonAction(ActionEvent event){
        if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty()){
            displayAlert("Error", "All fields must be filled.");
            return;
        }
        User newUser = new User(txtName.getText(), txtEmail.getText());
        userPlugin.addUser(newUser);
        refreshTable();
        cleanData();
    }

    @FXML
    private void UpdateButtonAction(ActionEvent event){
        if(txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtEmail.getText().isEmpty()){
            displayAlert("Error", "All fields must be filled.");
            return;
        }
        User updatedUser = new User(txtName.getText(), txtEmail.getText());
        updatedUser.setUserId(Integer.valueOf(txtId.getText()));
        userPlugin.updateUser(updatedUser);
        refreshTable();
        cleanData();
    }

    @FXML
    private void DeleteButtonAction(ActionEvent event){
        if(txtId.getText().isEmpty()){
            displayAlert("Error", "ID field must be filled.");
            return;
        }
        userPlugin.deleteUser(Integer.valueOf(txtId.getText()));
        refreshTable();
        cleanData();
    }
}


