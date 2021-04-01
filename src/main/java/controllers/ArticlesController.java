package controllers;

import app.App;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController {
    private App parent;
    private Connection connection;
    private List<Article> articles;
    private Article selectedArticle;
    private Stage articleStage;
    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<?, ?> articleIdCol;
    @FXML
    private TableColumn<?, ?> articleNameCol;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;

    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
        articleIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        articleNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        articles = new ArrayList<>();
        refreshTable();

        articleTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Article>) c -> {
            if (c.next()) {
                try {
                    selectedArticle = c.getAddedSubList().get(0);
                    modifyButton.setDisable(false);
                    deleteButton.setDisable(false);
                } catch (IndexOutOfBoundsException e) {
                    modifyButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            }
        });
    }

    public void refreshTable() {
        try {
            Statement stmt = connection.createStatement();

            articles.clear();
            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2)));
            }

            articleTable.getItems().clear();
            articleTable.getItems().addAll(articles);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void backClick() {
        closeArticleStage();
        parent.showMainWindow();
    }

    @FXML
    public void addClick() {
        closeArticleStage();
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("/article_operation.fxml"));
            articleStage = new Stage();
            articleStage.setScene(new Scene(loader.load()));
            articleStage.setTitle("Add article");
            articleStage.show();

            ArticleOperationController articleOperationController = loader.getController();
            articleOperationController.provideParent(this, connection, 0);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void deleteClick() {
        if (selectedArticle == null) return;
        try {
            Statement stmt = connection.createStatement();

            stmt.executeQuery("delete from ARTICLES a where a.id = " + selectedArticle.getId());
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void modifyClick() {
        closeArticleStage();
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("/article_operation.fxml"));
            articleStage = new Stage();
            articleStage.setScene(new Scene(loader.load()));
            articleStage.setTitle("Modify article");
            articleStage.show();

            ArticleOperationController articleOperationController = loader.getController();
            articleOperationController.provideParent(this, connection, selectedArticle.getId());
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    public void closeArticleStage() {
        if (articleStage != null) {
            articleStage.close();
        }
    }

    public void showErrorAlert(String message) {
        parent.showErrorAlert(message);
    }
}
