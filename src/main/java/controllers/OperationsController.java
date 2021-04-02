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
import model.Balance;
import model.Operation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OperationsController {
    private App parent;
    private Connection connection;
    private List<Operation> operations;
    private Operation selectedOperation;
    private Stage operationStage;

    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<?, ?> operationIdCol;
    @FXML
    private TableColumn<?, ?> operationArticleIdCol;
    @FXML
    private TableColumn<?, ?> operationDebitCol;
    @FXML
    private TableColumn<?, ?> operationCreditCol;
    @FXML
    private TableColumn<?, ?> operationCreditDateCol;
    @FXML
    private TableColumn<?, ?> operationBalanceIdCol;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;


    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
        operations = new ArrayList<>();
        operationIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        operationArticleIdCol.setCellValueFactory(new PropertyValueFactory<>("article"));
        operationDebitCol.setCellValueFactory(new PropertyValueFactory<>("debit"));
        operationCreditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        operationCreditDateCol.setCellValueFactory(new PropertyValueFactory<>("creditDate"));
        operationBalanceIdCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);
        refreshTable();

        operationTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Operation>) c -> {
            if (c.next()) {
                try {
                    selectedOperation = c.getAddedSubList().get(0);
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

            operations.clear();
            ResultSet rs = stmt.executeQuery("select * from OPERATIONS o join ARTICLES a on a.id=o.article_id join BALANCE b on b.id=o.balance_id");
            while (rs.next()) {
                operations.add(new Operation(rs.getInt(1), new Article(rs.getInt(7), rs.getString(8)),
                        rs.getInt(3), rs.getInt(4), rs.getDate(5), new Balance(rs.getInt(9), rs.getDate(10),
                        rs.getInt(11), rs.getInt(12), rs.getInt(13))));
            }

            operationTable.getItems().clear();
            operationTable.getItems().addAll(operations);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void addClick() {
        closeOperationStage();
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("/operation_operation.fxml"));
            operationStage = new Stage();
            operationStage.setScene(new Scene(loader.load()));
            operationStage.setTitle("Add Operation");
            operationStage.show();

            OperationOperationController operationOperationController = loader.getController();
            operationOperationController.provideParent(this, connection, null);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void modifyClick() {
        closeOperationStage();
        if (selectedOperation == null) return;
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("/operation_operation.fxml"));
            operationStage = new Stage();
            operationStage.setScene(new Scene(loader.load()));
            operationStage.setTitle("Modify Operation");
            operationStage.show();

            OperationOperationController operationOperationController = loader.getController();
            operationOperationController.provideParent(this, connection, selectedOperation);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void deleteClick() {
        if (selectedOperation == null) return;
        try {
            Statement stmt = connection.createStatement();

            stmt.executeQuery("delete from OPERATIONS o where o.id = " + selectedOperation.getId());
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void backClick() {
        closeOperationStage();
        parent.showMainWindow();
    }

    public void closeOperationStage() {
        if (operationStage != null) {
            operationStage.close();
        }
    }

    public void showErrorAlert(String message) {
        parent.showErrorAlert(message);
    }
}
