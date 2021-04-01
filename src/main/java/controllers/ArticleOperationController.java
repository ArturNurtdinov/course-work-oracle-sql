package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ArticleOperationController {
    private Connection connection;
    private ArticlesController parent;
    private int articleId;
    private Article obtainedArticle;

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;

    public void proveParent(ArticlesController parent, Connection connection, int articleId) {
        this.connection = connection;
        this.parent = parent;
        this.articleId = articleId;

        if (articleId != 0) {
            idLabel.setVisible(true);
            idText.setVisible(true);
            try {
                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select * from ARTICLES a where a.id = " + articleId);
                if (rs.next()) {
                    obtainedArticle = new Article(rs.getInt(1), rs.getString(2));
                    nameText.setText(obtainedArticle.getName());
                    idText.setText(obtainedArticle.getId() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            idLabel.setVisible(false);
            idText.setVisible(false);
        }
    }

    @FXML
    public void cancelClick() {
        parent.closeArticleStage();
    }

    @FXML
    public void performClick() {
        String newName = nameText.getText();
        if (newName.isBlank()) {
            parent.showErrorAlert("New name can't be blank!");
            return;
        }
        if (articleId != 0) {
            try {
                Statement stmt = connection.createStatement();
                stmt.executeQuery("update ARTICLES a set a.name = '" + newName + "' where a.id = " + articleId);
                parent.refreshTable();
                parent.closeArticleStage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Statement stmt = connection.createStatement();
                stmt.executeQuery("insert into ARTICLES (name) values ('" + newName + "')");
                parent.refreshTable();
                parent.closeArticleStage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
