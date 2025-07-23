package br.edu.ifba.inf008.plugins;

import java.util.List;

import br.edu.ifba.inf008.interfaces.IUserPlugin;
import br.edu.ifba.inf008.interfaces.models.User;

public class UserPluginImpl implements IUserPlugin {

    // Este é o cara que vai fazer o trabalho de verdade.
    // Vamos movê-lo para dentro do plugin no próximo passo.
    private final UserManager userManager = new UserManager();    

    @Override
    public User addUser(User user) {
        System.out.println("Plugin de Usuário: chamando addUser...");
        return this.userManager.addUser(user);
    }

    @Override
    public boolean deleteUser(Integer userId) {
        System.out.println("Plugin de Usuário: chamando deleteUser...");
        return this.userManager.deleteUser(userId);
    }

    @Override
    public boolean updateUser(User user) {
        System.out.println("Plugin de Usuário: chamando updateUser...");
        return this.userManager.updateUser(user);
    }

    @Override
    public User getUserById(Integer userId) {
        System.out.println("Plugin de Usuário: chamando getUserById...");
        return this.userManager.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("Plugin de Usuário: chamando getAllUsers...");
        return this.userManager.getAllUsers();
    }

    @Override
    public boolean init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
