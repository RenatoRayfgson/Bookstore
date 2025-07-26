package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.ILoanPlugin;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.models.Book;
import br.edu.ifba.inf008.interfaces.models.Loan;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.plugins.ui.NewLoanViewController;
import br.edu.ifba.inf008.plugins.ui.ReturnLoanViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
// A classe implementa IPlugin (para ser descoberta) e ILoanPlugin (para prover serviços)
public class LoanPlugin implements IPlugin, ILoanPlugin {

    // Instância do LoanManager independente, que contém toda a lógica de negócio.
    private final LoanManager loanManager;

    /**
     * O construtor agora é muito simples: apenas cria uma nova instância do LoanManager.
     */
    public LoanPlugin() {
        this.loanManager = new LoanManager();
    }

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        // --- Item de Menu 1: Registrar Novo Empréstimo ---
        MenuItem registerLoanItem = uiController.createMenuItem("Loan", "Register New Loan");
        
        registerLoanItem.setOnAction((ActionEvent e) -> {
            try {
                // ATENÇÃO: Verifique se o nome do seu FXML é "NewLoanView.fxml" ou "RegisterLoanView.fxml"
                String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/RegisterLoanView.fxml"; 
                URL fxmlUrl = getClass().getResource(fxmlPath);

                if (fxmlUrl == null) throw new IOException("Arquivo FXML não encontrado: " + fxmlPath);
                
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(fxmlUrl);
                fxmlLoader.setClassLoader(getClass().getClassLoader());
                
                Parent ui = fxmlLoader.load();
                
                NewLoanViewController controller = fxmlLoader.getController();
                controller.setPlugin(this);

                uiController.setMainContent(ui);

            } catch (IOException ex) {
                System.err.println("Falha ao carregar a UI de Registro de Empréstimo.");
                ex.printStackTrace();
            }
        });

        // --- Item de Menu 2: Retornar Empréstimo ---
        MenuItem returnLoanItem = uiController.createMenuItem("Loan", "Return Loan");

        returnLoanItem.setOnAction((ActionEvent e) -> {
            try {
                String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/ReturnLoanView.fxml";
                URL fxmlUrl = getClass().getResource(fxmlPath);

                if (fxmlUrl == null) throw new IOException("Arquivo FXML não encontrado: " + fxmlPath);
                
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(fxmlUrl);
                fxmlLoader.setClassLoader(getClass().getClassLoader());

                Parent ui = fxmlLoader.load();
                
                ReturnLoanViewController controller = fxmlLoader.getController();
                controller.setPlugin(this);

                uiController.setMainContent(ui);

            } catch (IOException ex) {
                System.err.println("Falha ao carregar a UI de Devolução de Empréstimo.");
                ex.printStackTrace();
            }
        });

        return true;
    }

    // --- Implementação COMPLETA dos métodos de serviço do ILoanPlugin ---
    // Eles agora simplesmente delegam a chamada para o loanManager.

    @Override
    public Loan registerLoan(Integer userId, Integer bookId, LocalDate dueDate) {
        return this.loanManager.registerLoan(userId, bookId, dueDate);
    }

    @Override
    public Loan getLoanById(Integer loanId) {
        return this.loanManager.getLoanById(loanId);
    }

    @Override
    public Loan registerReturn(Integer loanId) {
        return this.loanManager.registerReturn(loanId);
    }

    @Override
    public List<Loan> getActiveLoans() {
        return this.loanManager.getActiveLoans();
    }

    @Override
    public List<Loan> getAllLoans() {
        return this.loanManager.getAllLoans();
    }

    @Override
    public List<Loan> getLoansByUserId(Integer userId) {
        return this.loanManager.getLoansByUserId(userId);
    }

    @Override
    public Boolean updateLoan(Loan loan) {
        return this.loanManager.updateLoan(loan);
    }

    @Override
    public Boolean deleteLoan(Integer loanId) {
        return this.loanManager.deleteLoan(loanId);
    }

    @Override
    public void markOverduedLoans() {
        this.loanManager.markOverduedLoans();
    }

    // Métodos para prover dados de User e Book de forma independente
    @Override
    public List<User> getAllUsers() {
        return this.loanManager.getAllUsers();
    }

    @Override
    public List<Book> getAllBooks() {
        return this.loanManager.getAllBooks();
    }
}