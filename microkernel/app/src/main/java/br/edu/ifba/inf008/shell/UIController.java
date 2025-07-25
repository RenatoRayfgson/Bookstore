package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class UIController extends Application implements IUIController
{
    private ICore core;
    private MenuBar menuBar;
    private TabPane tabPane;
    private static UIController uiController;
    private BorderPane mainLayout;

    public UIController() {
    }

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bookstore Manager");
        
        mainLayout = new BorderPane();
        
        menuBar = new MenuBar();
        mainLayout.setTop(menuBar);

        Scene scene = new Scene(mainLayout, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        Core.getInstance().getPluginController().init();
    }

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText() == menuText) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }
        
        MenuItem menuItem = new MenuItem(menuItemText);
        newMenu.getItems().add(menuItem);

        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        Tab tab = new Tab();
        tab.setText(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);

        return true;
    }

    public void setMainContent(Node node) {
        mainLayout.setCenter(node);
    }
}
