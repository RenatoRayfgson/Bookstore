package br.edu.ifba.inf008.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IReportPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.models.ReportDetails;
import br.edu.ifba.inf008.plugins.ui.ReportViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
public class ReportPlugin implements IPlugin, IReportPlugin {

    private final ReportManager reportManager = new ReportManager();

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean init() {
    IUIController uiController = ICore.getInstance().getUIController();

    MenuItem menuItem = uiController.createMenuItem("Reports", "Books Loaned");

    menuItem.setOnAction((ActionEvent e) -> {
        try {            
            String fxmlPath = "/br/edu/ifba/inf008/plugins/ui/ReportView.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);
            
            FXMLLoader fxmlLoader = new FXMLLoader();            
            
            fxmlLoader.setLocation(fxmlUrl);            
            
            fxmlLoader.setClassLoader(getClass().getClassLoader());
            
            Parent reportUi = fxmlLoader.load();

            ReportViewController controller = fxmlLoader.getController();
            controller.setReportPlugin(this);

            uiController.setMainContent(reportUi);

        } catch (IOException ex) {
            System.err.println("Something went wrong while loading the Report UI.");
            ex.printStackTrace();
            }
        });

        return true;
    }    

    @Override
    public List<ReportDetails> generateLoanReport() {
        return reportManager.generateLoanReport();
    }
}