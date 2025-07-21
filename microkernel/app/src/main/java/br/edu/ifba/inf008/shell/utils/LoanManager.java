package br.edu.ifba.inf008.shell.utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifba.inf008.shell.models.Book;
import br.edu.ifba.inf008.shell.models.Book.LoanStatus;
import br.edu.ifba.inf008.shell.models.Loan;
import br.edu.ifba.inf008.shell.models.User;




public class LoanManager {

    private final BookManager bookManager;
    private final UserManager userManager;
    private final Map<Integer, Loan> activeLoansInMemory = new HashMap<>();

    public LoanManager(BookManager bookManager, UserManager userManager) {
        this.bookManager = bookManager;
        this.userManager = userManager;

    }

    public Loan registerLoan(Integer userID, Integer bookID, LocalDate dueDate){
        if(userID == null || bookID == null || dueDate == null){
            System.out.println("Something wrong has happened.");
            return null;
        }
        Connection connection = null;
        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);

            User user = userManager.getUserById(userID);
            if(user == null) {
                System.out.println("User not found.");
                connection.rollback();
                return null;
            }
            Book book = bookManager.getBookByID(bookID);
            if(book == null) {
                System.out.println("Book not found.");
                connection.rollback();
                return null;
            }
            if(!bookManager.isBookAvailable(bookID)) {
                System.out.println("No copies available for this book.");
                connection.rollback();
                return null;
            }
            if(!bookManager.decrementAvailableCopies(bookID)){
                System.out.println("Failed to decrement available copies.");
                connection.rollback();
                return null;
            }

            String sql = "INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                LocalDate loanDate = LocalDate.now();
                pstmt.setInt(1, userID);
                pstmt.setInt(2, bookID);
                pstmt.setObject(3, loanDate);
                pstmt.setObject(4, null);

                int affectedRows = pstmt.executeUpdate();
                if(affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()){
                        if(rs.next()){
                            Integer loanID = rs.getInt(1);
                            Loan newLoan = new Loan(loanID, userID, bookID, loanDate, dueDate, null, LoanStatus.ACTIVE);
                            activeLoansInMemory.put(loanID, newLoan);
                            connection.commit();
                            System.out.println("Loan registered successfully.");
                            return newLoan;
                        }
                    }
                }
            }
            connection.rollback();
            System.out.println("Something went wrong.");
            return null;
        } catch (SQLException e) {
            System.err.println("Error while trying to register the loan: " + e.getMessage());
            try {
                if(connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.err.println("Error while trying to rollback the transaction: " + e2.getMessage());
            }
            return null;
        } finally {
            try {
                if(connection != null){
                    connection.setAutoCommit(true);                    
                }
                if(connection != null){
                    connection.close();                    
                }
            } catch (SQLException e) {
                System.err.println("Error while trying to close the connection: " + e.getMessage());
            }
        }
    }

    public Loan getLoanById(Integer loanId){
        if(loanId == null){
            System.out.println("Loan Id not found.");
            return null;
        }
        if(activeLoansInMemory.containsKey(loanId)){
            return activeLoansInMemory.get(loanId);
        }
        String sql = "SELECT loan_id, user_id, book_id, loan_date, return_date FROM loans WHERE loan_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, loanId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getObject("loan_date", LocalDate.class));
                    loan.setReturnDate(rs.getObject("return_date", LocalDate.class));
                    
                    if(loan.getReturnDate() != null){
                        loan.setStatus(LoanStatus.RETURNED);
                    }else{
                        LocalDate inferredDueDate = loan.getLoanDate().plusDays(14);
                        loan.setDueDate(inferredDueDate);
                        if(inferredDueDate.isBefore(LocalDate.now())) {
                            loan.setStatus(LoanStatus.OVERDUE);
                        } else {
                            loan.setStatus(LoanStatus.ACTIVE);
                        }
                    }
                    return loan;
                }
            }
        }catch (SQLException e) {
            System.err.println("Error while trying to get the loan by ID: " + e.getMessage());
        }
        return null;
    }

    public Loan registerReturn(Integer loanID){
        if(loanID == null){
            System.out.println("The loanID was not found");
            return null;
        }
        Connection connection = null;
        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            Loan loan = getLoanById(loanID);
            if(loan == null) {
                System.out.println("Loan not found.");
                connection.rollback();
                return null;
            }
            if(loan.getReturnDate() != null || loan.getStatus() == LoanStatus.RETURNED) {
                System.out.println("This loan has already been returned.");
                connection.rollback();
                return null;
            }
            LocalDate currentReturnDate = LocalDate.now();
            String sql = "UPDATE loans SET return_date = ? WHERE loan_id = ?";
            try(PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setObject(1, currentReturnDate);
                pstmt.setInt(2, loanID);
                int affectedRows = pstmt.executeUpdate();
                if(affectedRows > 0) {
                    if(!bookManager.incrementAvailableCopies(loan.getBookId())) {
                        System.out.println("Something went wrong at incrementing available copies.");
                        connection.rollback(); //Decidi fazer rollback, mas talvez seja uma decisão mais inteligente commitar
                        return null;
                    }
                    loan.setReturnDate(currentReturnDate);
                    loan.setStatus(LoanStatus.RETURNED);
                    activeLoansInMemory.remove(loanID);
                    connection.commit();
                    System.out.println("Loan returned successfully.");
                    return loan;
                }else{
                    System.out.println("Something went wrong while trying to return the loan.");
                    connection.rollback();
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while trying to return the loan: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error while trying to rollback the connection: " + ex.getMessage());
            }
            return null;
        } finally {
            try {
                if(connection != null){
                    connection.setAutoCommit(true);
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while trying to close the connection: " + e.getMessage());
            }
        }
    }

    public List<Loan> getActiveLoans(){
        List<Loan> activeLoans = new ArrayList<>();
        String sql = "SELECT loan_id, user_id, book_id, loan_date, return_date FROM loans WHERE return_date IS NULL";
        try(Connection connection = DBConnector.getConnection();){

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setLoanDate(rs.getObject("loan_date", LocalDate.class));
                loan.setReturnDate(null);
                
                LocalDate inferredDueDate = loan.getLoanDate().plusDays(14);
                loan.setDueDate(inferredDueDate);
                if(inferredDueDate.isBefore(LocalDate.now())) {
                    loan.setStatus(LoanStatus.OVERDUE);
                } else {
                    loan.setStatus(LoanStatus.ACTIVE);
                }                
                activeLoans.add(loan);
            }
        }catch (SQLException e) {
            System.err.println("Error while trying to retrieve active loans: " + e.getMessage());
            return activeLoans;
        }
        return activeLoans;
    }
    
    public List<Loan> getAllLoans(){
        List<Loan> allLoans = new ArrayList<>();
        String sql = "SELECT loan_id, user_id, book_id, loan_date, return_date FROM loans WHERE return_date IS NULL";
        try(Connection connection = DBConnector.getConnection();){

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setLoanDate(rs.getObject("loan_date", LocalDate.class));
                loan.setReturnDate(rs.getObject("return_date", LocalDate.class));
                
                if(loan.getReturnDate() != null) {
                    loan.setStatus(LoanStatus.RETURNED);
                } else {
                    LocalDate inferredDueDate = loan.getLoanDate().plusDays(14);
                    loan.setDueDate(inferredDueDate);
                    if(inferredDueDate.isBefore(LocalDate.now())) {
                        loan.setStatus(LoanStatus.OVERDUE);
                    }else{
                        loan.setStatus(LoanStatus.ACTIVE);
                    }
                }
                allLoans.add(loan);
            }
        }catch (SQLException e) {
            System.err.println("Error while trying to retrieve all loans: " + e.getMessage());
            return allLoans;
        }
        return allLoans;
    }

    public List<Loan> getLoansByUserId(Integer userId){
        List<Loan>loansByUser = new ArrayList<>();
        String sql = "SELECT loan_id, user_id, book_id, loan_date, return_date FROm loans WHERE user_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getObject("loan_date", LocalDate.class));
                    loan.setReturnDate(rs.getObject("return_date", LocalDate.class));

                    if(loan.getReturnDate() != null) {
                        loan.setStatus(LoanStatus.RETURNED);
                    } else {
                        LocalDate inferredDueDate = loan.getLoanDate().plusDays(14);
                        loan.setDueDate(inferredDueDate);
                        if(inferredDueDate.isBefore(LocalDate.now())) {
                            loan.setStatus(LoanStatus.OVERDUE);
                        } else {
                            loan.setStatus(LoanStatus.ACTIVE);
                        }
                    }
                    loansByUser.add(loan);
                }
            }
        }catch (SQLException e) {
            System.err.println("Error while trying to retrieve loans by user ID: " + e.getMessage());            
        }
        return loansByUser;
    }

    public Boolean updateLoan(Loan loan){
        String sql = "UPDATE loans SET user_id = ?, book_id = ?, loan_date = ?, return_date = ? WHERE loan_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, loan.getUserId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setObject(3, loan.getLoanDate());
            pstmt.setObject(4, loan.getReturnDate());
            pstmt.setInt(5, loan.getLoanId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e) {
            System.err.println("Error while trying to update loan: " + e.getMessage());
            return false;
        }
    }

    public Boolean deleteLoan(Integer loanId){

        if(loanId == null){
            System.out.println("Loan do not exists.");
            return false;
        }
        Loan loanToDelete = getLoanById(loanId);
        if(loanToDelete == null){
            System.out.println("Loan not found.");
            return false;
        }

        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try(Connection connection = DBConnector.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, loanId);
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Loan deleted successfully.");
                if(loanToDelete.getStatus() == LoanStatus.ACTIVE || loanToDelete.getStatus() == LoanStatus.OVERDUE) {
                    bookManager.incrementAvailableCopies(loanToDelete.getBookId());
                    System.out.println("The book was retuned and is available again.");
                }
                activeLoansInMemory.remove(loanId);
                return true;
            }
            return false;
        }catch (SQLException e) {
            System.err.println("Error while trying to delete the loan: " + e.getMessage());
            return false;
        }

    }

    //Auxiliar
    public void markOverduedLoans(){
        LocalDate today = LocalDate.now();
        List<Loan> activeLoans = getActiveLoans();
        for(Loan loan : activeLoans){
            if(loan.getReturnDate() == null && loan.getDueDate() != null){
                if(loan.getDueDate().isBefore(today)){
                    if(loan.getStatus() != LoanStatus.OVERDUE) {
                        System.out.println("Loan with ID " + loan.getLoanId() + " is overdue.");
                        loan.setStatus(LoanStatus.OVERDUE);
                    }
                
                }else{
                    if(loan.getStatus() == LoanStatus.OVERDUE && !loan.getDueDate().isBefore(today)) {
                        loan.setStatus(LoanStatus.ACTIVE);
                    }
                }
            }
        }
    }

}
