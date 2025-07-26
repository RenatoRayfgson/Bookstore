package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.ILoanPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.models.Loan;
import br.edu.ifba.inf008.plugins.ui.NewLoanViewController;
import br.edu.ifba.inf008.plugins.ui.ReturnLoanViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
// No futuro, esta classe também implementará sua interface de serviço, ex: ILoanPlugin
public class LoanPlugin implements ILoanPlugin {

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean init() {
    IUIController uiController = ICore.getInstance().getUIController();

    // --- Item de Menu 1: Registrar Novo Empréstimo ---
    MenuItem registerLoanItem = uiController.createMenuItem("Loan", "Register New Loan");
    
    registerLoanItem.setOnAction((ActionEvent e) -> {
        try {
            String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/RegisterLoanView.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            // Verificação para garantir que o arquivo FXML foi encontrado
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado no caminho: " + fxmlPath);
            }
            
            // --- CÓDIGO CORRIGIDO ---
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

            // Verificação para garantir que o arquivo FXML foi encontrado
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado no caminho: " + fxmlPath);
            }
            
            // --- CÓDIGO CORRIGIDO ---
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

    @Override
    public Loan registerLoan(Integer userId, Integer bookId, LocalDate dueDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Loan getLoanById(Integer loanId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Loan registerReturn(Integer loanId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Loan> getActiveLoans() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Loan> getAllLoans() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Loan> getLoansByUserId(Integer userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean updateLoan(Loan loan) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteLoan(Integer loanId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void markOverduedLoans() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}