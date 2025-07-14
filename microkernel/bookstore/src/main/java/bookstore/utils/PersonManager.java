package bookstore.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bookstore.models.Person;

public class PersonManager {

    public Person addPerson(Person client){
        String sql = "INSERT INTO person (name, email) VALUES (?, ?)";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getEmail());
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        client.setPersonId(rs.getInt(1));
                    }
                }
            }
            return client;
        } catch (SQLException e) {
            System.err.println("Error while trying to add the new client: " + e.getMessage());
            return null;
        }

    }
}
