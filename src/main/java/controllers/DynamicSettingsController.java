package controllers;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import model.Article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DynamicSettingsController {
    private App parent;
    private Connection connection;

    public List<Article> articles;

    @FXML
    public ListView<Article> articleList;
    @FXML
    public DatePicker fromDate;
    @FXML
    public DatePicker toDate;

    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
        articles = new ArrayList<>();
        articleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fillArticles();
        fillArticlesList();
    }

    private void fillArticlesList() {
        articleList.getItems().addAll(articles);
    }

    private void fillArticles() {
        try {
            Statement stmt = connection.createStatement();

            articles.clear();
            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void debitsClick() {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        if (from == null || to == null) {
            parent.showErrorAlert("Dates are empty!");
            return;
        }
        List<Article> articles = articleList.getSelectionModel().getSelectedItems();
        if (articles.isEmpty()) {
            parent.showErrorAlert("Articles are empty!");
            return;
        }
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/dynamic_flow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dynamic of debits");
            stage.show();

            DynamicFlowController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection, from, to, articles, "Debit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void creditsClick() {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        if (from == null || to == null) {
            parent.showErrorAlert("Dates are empty!");
            return;
        }
        List<Article> articles = articleList.getSelectionModel().getSelectedItems();
        if (articles.isEmpty()) {
            parent.showErrorAlert("Articles are empty!");
            return;
        }
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/dynamic_flow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dynamic of credits");
            stage.show();

            DynamicFlowController mainScreenController = loader.getController();
            mainScreenController.provideApp(this, connection, from, to, articles, "Credit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backClick() {
        parent.showMainWindow();
    }
}
