package controllers;

import app.App;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Article;
import model.Balance;
import model.Operation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalancesController {
    private App parent;
    private Connection connection;
    private List<Balance> balances;
    private Balance selectedBalance;

    @FXML
    private TableView<Balance> balanceTable;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private TableColumn<?, ?> debitCol;
    @FXML
    private TableColumn<?, ?> creditCol;
    @FXML
    private TableColumn<?, ?> amountCol;
    @FXML
    private Button formButton;

    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
        balances = new ArrayList<>();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        debitCol.setCellValueFactory(new PropertyValueFactory<>("debit"));
        creditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        formButton.setDisable(true);
        refreshTable();

        balanceTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Balance>) c -> {
            if (c.next()) {
                try {
                    selectedBalance = c.getAddedSubList().get(0);
                    formButton.setDisable(false);
                } catch (IndexOutOfBoundsException e) {
                    formButton.setDisable(true);
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshTable() {
        try {
            Statement stmt = connection.createStatement();

            balances.clear();
            ResultSet rs = stmt.executeQuery("select * from BALANCE");
            while (rs.next()) {
                balances.add(new Balance(rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5)));
            }

            balanceTable.getItems().clear();
            balanceTable.getItems().addAll(balances);
        } catch (Exception e) {
            parent.showErrorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void formClick() {
        if (selectedBalance == null) return;
        try {
            int sumDebit = 0;
            int sumCredit = 0;
            Date dateFrom = Date.valueOf(selectedBalance.getCreateDate().toLocalDate().minusDays(1));
            Date dateTo = Date.valueOf(LocalDate.now());
            PreparedStatement stmt = connection.prepareStatement("select sum(o.debit) from operations o where o.balance_id = ? and o.credit_date between ? and ?");
            stmt.setInt(1, selectedBalance.getId());
            stmt.setDate(2, dateFrom);
            stmt.setDate(3, dateTo);
            ResultSet rs1 = stmt.executeQuery();
            if (rs1.next()) {
                sumDebit = rs1.getInt(1);
            }

            stmt = connection.prepareStatement("select sum(o.credit) from operations o where o.balance_id = ? and o.credit_date between ? and ?");
            stmt.setInt(1, selectedBalance.getId());
            stmt.setDate(2, dateFrom);
            stmt.setDate(3, dateTo);
            ResultSet rs2 = stmt.executeQuery();
            if (rs2.next()) {
                sumCredit = rs2.getInt(1);
            }

            stmt = connection.prepareStatement("update balance b set b.create_date = ?, b.debit = b.debit + ?, b.credit = b.credit + ?, b.amount = b.amount + ? where b.id = ?");
            stmt.setDate(1, dateTo);
            stmt.setInt(2, sumDebit);
            stmt.setInt(3, sumCredit);
            stmt.setInt(4, sumDebit - sumCredit);
            stmt.setInt(5, selectedBalance.getId());
            stmt.executeUpdate();
            refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    @FXML
    public void backClick() {
        parent.showMainWindow();
    }
}
