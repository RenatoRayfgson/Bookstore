package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

@SuppressWarnings("unused")
public class LoanPlugin implements IPlugin
{
    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Loan", "Loan Management");
        menuItem.setOnAction((ActionEvent e) -> { //Linha antiga: menuItem.setOnAction(new EventHandler<ActionEvent>()
            System.out.println("I've been clicked!");
        });

        return true;
    }
}
