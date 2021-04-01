package app;

import controllers.ArticlesController;
import controllers.BalancesController;
import controllers.MainScreenController;
import controllers.OperationsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class App extends Application {

    private Stage primaryStage;
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            //DriverManager.registerDriver(new OracleDriver());

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "c##artur", "1234");

            showMainWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMainWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainscreen.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Menu");
            primaryStage.show();

            MainScreenController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showArticlesWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/articles.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Articles");
            primaryStage.show();

            ArticlesController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOperationsWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/operations.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Operations");
            primaryStage.show();

            OperationsController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBalancesWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/balances.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Balances");
            primaryStage.show();

            BalancesController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showErrorAlert(String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong. Please read information below.");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
