package br.edu.ifba.inf008.plugins;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.ReportDetails;
import br.edu.ifba.inf008.interfaces.utils.DBConnector;

public class ReportManager {   

    public List<ReportDetails> generateLoanReport() {
        List<ReportDetails> reportData = new ArrayList<>();
        
        String sql = "SELECT " +
                     " l.loan_id, " +
                     " b.title AS book_title, " +
                     " b.author AS book_author, " +
                     " u.name AS client_name, " +
                     " l.loan_date " +
                     "FROM " +
                     " loans l " +
                     "JOIN " +
                     " books b ON l.book_id = b.book_id " +
                     "JOIN " +
                     " users u ON l.user_id = u.user_id " +
                     "WHERE " +
                     " l.return_date IS NULL " +
                     "ORDER BY " +
                     " l.loan_date DESC";

        try (Connection connection = DBConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {                
                ReportDetails detail = new ReportDetails(
                    rs.getInt("loan_id"),
                    rs.getString("book_title"),
                    rs.getString("book_author"),
                    rs.getString("client_name"),
                    rs.getObject("loan_date", LocalDate.class)
                );
                reportData.add(detail);
            }

        } catch (SQLException e) {
            System.err.println("Error while generating the loan report: " + e.getMessage());
        }
        
        return reportData;
    }

}