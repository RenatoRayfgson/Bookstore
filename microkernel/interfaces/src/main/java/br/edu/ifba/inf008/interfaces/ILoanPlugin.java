package br.edu.ifba.inf008.interfaces;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.interfaces.models.Loan;

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
}
