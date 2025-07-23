package br.edu.ifba.inf008.interfaces;

import java.util.List;

import br.edu.ifba.inf008.interfaces.models.User;

public interface IUserPlugin extends IPlugin {

    User addUser(User user);
    
    boolean deleteUser(Integer userId);
    
    boolean updateUser(User user);
    
    User getUserById(Integer userId);
    
    List<User> getAllUsers();
}

