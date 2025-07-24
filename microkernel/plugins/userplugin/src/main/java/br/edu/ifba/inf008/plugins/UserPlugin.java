package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.IUserPlugin;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.plugins.ui.UserViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

// A classe do plugin agora também implementa sua própria interface de serviço
public class UserPlugin implements IUserPlugin {

    private final UserManager userManager = new UserManager();

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean init() {
    IUIController uiController = ICore.getInstance().getUIController();

    MenuItem menuItem = uiController.createMenuItem("User", "User Management");

    menuItem.setOnAction((ActionEvent e) -> {
        try {
            // --- INÍCIO DA CORREÇÃO ---

            // 1. Encontra o caminho para o FXML, como antes.
            String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/UserView.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            // 2. Cria uma INSTÂNCIA do FXMLLoader, em vez de usar o método estático.
            FXMLLoader fxmlLoader = new FXMLLoader();
            
            // 3. Define a localização do FXML.
            fxmlLoader.setLocation(fxmlUrl);
            
            // 4. ESTA É A LINHA MÁGICA: Diz ao FXMLLoader para usar o ClassLoader deste plugin.
            fxmlLoader.setClassLoader(getClass().getClassLoader());

            // 5. Carrega a UI usando a instância configurada.
            Parent userUi = fxmlLoader.load();
            
            // --- FIM DA CORREÇÃO ---

            // O resto do código continua igual.
            UserViewController controller = fxmlLoader.getController();
            controller.setUserPlugin(this);

            uiController.createTab("User", userUi);

        } catch (IOException ex) {
            System.err.println("Falha ao carregar a UI do plugin de usuário.");
            ex.printStackTrace();
        }
    });

    return true;
}

    // --- Implementação dos métodos de serviço do IUserPlugin ---
    // A classe agora delega as chamadas para o seu próprio UserManager.

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