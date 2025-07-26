package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.IUserPlugin;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.plugins.ui.UserViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
public class UserPlugin implements IPlugin, IUserPlugin {

    private final UserManager userManager = new UserManager();

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean init() {
    IUIController uiController = ICore.getInstance().getUIController();

    MenuItem menuItem = uiController.createMenuItem("User", "User Management");

    menuItem.setOnAction((ActionEvent e) -> {
        try {            
            String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/UserView.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);
            
            FXMLLoader fxmlLoader = new FXMLLoader();            
            
            fxmlLoader.setLocation(fxmlUrl);            
            
            fxmlLoader.setClassLoader(getClass().getClassLoader());
            
            Parent userUi = fxmlLoader.load();
            
            UserViewController controller = fxmlLoader.getController();
            controller.setUserPlugin(this);

            uiController.setMainContent(userUi);

        } catch (IOException ex) {
            System.err.println("Falha ao carregar a UI do plugin de usuário.");
            ex.printStackTrace();
            }
        });

        return true;
    }
    

    @Override
    public User addUser(User user) {
        return userManager.addUser(user);
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userManager.deleteUser(userId);
    }

    @Override
    public boolean updateUser(User user) {
        return userManager.updateUser(user);
    }

    @Override
    public User getUserById(Integer userId) {
        return userManager.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userManager.getAllUsers();
    }
}