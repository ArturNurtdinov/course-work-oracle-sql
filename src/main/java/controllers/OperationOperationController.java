package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Article;
import model.Balance;
import model.Operation;
import utils.ListUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperationOperationController {
    private Connection connection;
    private OperationsController parent;
    private Operation operation;
    private List<Article> articles;
    private List<Balance> balances;

    @FXML
    private Label idLabel;
    @FXML
    private Label articleLabel;
    @FXML
    private Label debitLabel;
    @FXML
    private Label creditLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label balanceLabel;


    @FXML
    private TextField idText;
    @FXML
    private ComboBox<Article> articleCombo;
    @FXML
    private TextField debitText;
    @FXML
    private TextField creditText;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Balance> balanceCombo;


    public void provideParent(OperationsController parent, Connection connection, Operation operation) {
        this.connection = connection;
        this.parent = parent;
        this.operation = operation;
        articles = new ArrayList<>();
        balances = new ArrayList<>();
        refreshArticlesCombo();
        refreshBalancesCombo();
        if (operation != null) {
            datePicker.setValue(operation.getCreditDate().toLocalDate());
            idLabel.setVisible(true);
            idText.setVisible(true);
            idText.setText(operation.getId() + "");
            idText.setDisable(true);
            debitText.setText(operation.getDebit() + "");
            creditText.setText(operation.getCredit() + "");

            try {
                Article article = null;
                Balance balance = null;
                Statement stmt1 = connection.createStatement();

                ResultSet rs1 = stmt1.executeQuery("select * from articles a where a.id = " + operation.getArticle().getId());
                if (rs1.next()) {
                    article = new Article(rs1.getInt(1), rs1.getString(2));
                }

                Statement stmt2 = connection.createStatement();

                ResultSet rs2 = stmt2.executeQuery("select * from balance b where b.id = " + operation.getBalance().getId());
                if (rs2.next()) {
                    balance = new Balance(rs2.getInt(1), rs2.getDate(2),
                            rs2.getInt(3), rs2.getInt(4), rs2.getInt(5));
                }

                articleCombo.getSelectionModel().select(ListUtils.find(articles, article));
                balanceCombo.getSelectionModel().select(ListUtils.find(balances, balance));
            } catch (Exception e) {
                e.printStackTrace();
                parent.showErrorAlert(e.getMessage());
            }
        } else {
            datePicker.setValue(LocalDate.now());
            idLabel.setVisible(false);
            idText.setVisible(false);
        }
    }


    private void refreshArticlesCombo() {
        try {
            Statement stmt = connection.createStatement();

            articles.clear();
            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2)));
            }

            articleCombo.getItems().clear();
            articleCombo.getItems().addAll(articles);
            articleCombo.getSelectionModel().select(0);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    private void refreshBalancesCombo() {
        try {
            Statement stmt = connection.createStatement();

            balances.clear();
            ResultSet rs = stmt.executeQuery("select * from balance");
            while (rs.next()) {
                balances.add(new Balance(rs.getInt(1), rs.getDate(2),
                        rs.getInt(3), rs.getInt(4), rs.getInt(5)));
            }

            balanceCombo.getItems().clear();
            balanceCombo.getItems().addAll(balances);
            balanceCombo.getSelectionModel().select(0);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void cancelClick() {
        parent.closeOperationStage();
    }

    @FXML
    public void performClick() {
        if (operation == null) {
            handleAddEvent();
        } else {
            handleModifyEvent();
        }
    }

    private void handleAddEvent() {
        Article article = articleCombo.getSelectionModel().getSelectedItem();
        if (article == null) {
            parent.showErrorAlert("Article must be chosen!");
            return;
        }

        Balance balance = balanceCombo.getSelectionModel().getSelectedItem();
        if (balance == null) {
            parent.showErrorAlert("Balance must be chosen!");
            return;
        }

        try {
            int debit = Integer.parseInt(debitText.getText());
            int credit = Integer.parseInt(creditText.getText());
            if (debit < 0 || credit < 0) {
                parent.showErrorAlert("Debit and credit must be positive");
                return;
            }
            LocalDate date = datePicker.getValue();

            if (date.isBefore(balance.getCreateDate().toLocalDate())) {
                parent.showErrorAlert("Invalid date. Balance for this date was already formed");
                return;
            }

            balances.clear();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into operations (article_id, debit, credit, credit_date, balance_id) values (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, article.getId());
            preparedStatement.setInt(2, debit);
            preparedStatement.setInt(3, credit);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setInt(5, balance.getId());
            preparedStatement.executeUpdate();
            parent.refreshTable();
            parent.closeOperationStage();
        } catch (NumberFormatException e) {
            parent.showErrorAlert("Invalid input for debit or credit");
            e.printStackTrace();
        } catch (Exception e) {
            parent.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleModifyEvent() {
        Article article = articleCombo.getSelectionModel().getSelectedItem();
        if (article == null) {
            parent.showErrorAlert("Article must be chosen!");
            return;
        }

        Balance balance = balanceCombo.getSelectionModel().getSelectedItem();
        if (balance == null) {
            parent.showErrorAlert("Balance must be chosen!");
            return;
        }

        try {
            int debit = Integer.parseInt(debitText.getText());
            int credit = Integer.parseInt(creditText.getText());
            if (debit < 0 || credit < 0) {
                parent.showErrorAlert("Debit and credit must be positive");
                return;
            }
            LocalDate date = datePicker.getValue();

            System.out.println(Date.valueOf(date));
            System.out.println(balance.getCreateDate().toLocalDate());
            if (date.isBefore(balance.getCreateDate().toLocalDate())) {
                parent.showErrorAlert("Invalid date. Balance for this date was already formed");
                return;
            }

            balances.clear();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update operations o set o.article_id = ?, o.debit = ?, o.credit = ?, o.credit_date = ?, o.balance_id = ? where o.id = ?");
            preparedStatement.setInt(1, article.getId());
            preparedStatement.setInt(2, debit);
            preparedStatement.setInt(3, credit);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setInt(5, balance.getId());
            preparedStatement.setInt(6, operation.getId());
            preparedStatement.executeUpdate();
            parent.refreshTable();
            parent.closeOperationStage();
        } catch (NumberFormatException e) {
            parent.showErrorAlert("Invalid input for debit or credit");
            e.printStackTrace();
        } catch (Exception e) {
            parent.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }
}
