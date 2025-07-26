package br.edu.ifba.inf008.interfaces;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.interfaces.models.Loan;
import br.edu.ifba.inf008.interfaces.models.User;

public interface ILoanPlugin extends IPlugin {

    Loan registerLoan(Integer userId, Integer bookId, LocalDate dueDate);
    
    Loan getLoanById(Integer loanId);
    
    Loan registerReturn(Integer loanId);
    
    List<Loan> getActiveLoans();
    
    List<Loan> getAllLoans();
    
    List<Loan> getLoansByUserId(Integer userId);
    
    Boolean updateLoan(Loan loan);
    
    Boolean deleteLoan(Integer loanId);
    
    void markOverduedLoans();

    List<User> getAllUsers();

    List<Book> getAllBooks();

}
