package controllers;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.Connection;

public class MainScreenController {
    private App parent;
    private Connection connection;
    @FXML
    private Button articlesButton;
    @FXML
    private Button operationsButton;
    @FXML
    private Button balancesButton;

    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
    }

    @FXML
    public void articlesClick() {
        parent.showArticlesWindow();
    }

    @FXML
    public void operationsClick() {
        parent.showOperationsWindow();
    }

    @FXML
    public void balancesClick() {
        parent.showBalancesWindow();
    }

    @FXML
    public void articlesFlowClick() {
        parent.showArticlesFlowWindow();
    }
}
