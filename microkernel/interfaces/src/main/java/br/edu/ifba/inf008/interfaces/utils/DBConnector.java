package br.edu.ifba.inf008.interfaces.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String JDBC_URL = "jdbc:mariadb://127.0.0.1:3307/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        // Garante que o driver JDBC é carregado
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC Driver not found: " + e.getMessage());
            throw new SQLException("MariaDB JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
