package bookstore.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bookstore.models.User;

public class PersonManager {

    public User addPerson(User client){
        String sql = "INSERT INTO users (name, email, registered_at) VALUES (?, ?, ?)";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getEmail());
            pstmt.setObject(3, LocalDate.now()); // Assuming you want to set the current date as registeredAt
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        client.setUserId(rs.getInt(1));
                    }
                }
            }
            return client;
        } catch (SQLException e) {
            System.err.println("Error while trying to add the new client: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteUser(Integer userId){
        String sql = "DELETE FROM users WHERE user_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setObject(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            } catch (SQLException e){
            System.err.println("Error while trying to delete the user: " + e.getMessage());
            return false;
            }
    }

    public boolean updateUser(User client){
        String sql = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getEmail());
            pstmt.setObject(3, client.getUserId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error while trying to update the user: " + e.getMessage());
            return false;
        }        
    }

    public User getUserById(Integer userId){
        String sql = "SELECT user_id, name, email, registered_at FROM users WHERE user_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setObject(1, userId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    User person = new User();
                    person.setUserId(rs.getObject("user_id", Integer.class));
                    person.setName(rs.getString("name"));
                    person.setEmail(rs.getString("email"));
                    person.setRegisteredAt(rs.getObject("registered_at", LocalDate.class));
                    return person;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while trying to retrieve the user: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers(){
        List<User> persons = new ArrayList<>();
        String sql = "SELECT user_id, name, email, registered_at FROM users";
        try(Connection connection = DBConnector.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                User person = new User();
                person.setUserId(rs.getObject("user_id", Integer.class));
                person.setName(rs.getString("name"));
                person.setEmail(rs.getString("email"));
                person.setRegisteredAt(rs.getObject("registered_at", LocalDate.class));
                persons.add(person);
            }
        }catch (SQLException e) {
            System.err.println("Error while trying to retrieve all persons: " + e.getMessage());
        }
        return persons;
    }
}