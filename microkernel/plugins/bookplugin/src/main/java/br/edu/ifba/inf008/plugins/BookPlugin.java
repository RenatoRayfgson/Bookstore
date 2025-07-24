package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BookPlugin implements IPlugin
{
    @Override //Adicionado depois
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Book", "Book Management");
        menuItem.setOnAction((ActionEvent e) -> { //Linha antiga: menuItem.setOnAction(new EventHandler<ActionEvent>()
            System.out.println("I've been clicked!");
        });

        uiController.createTab("Book", new Rectangle(200,200, Color.LIGHTSTEELBLUE));

        return true;
    }
}
