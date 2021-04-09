package controllers;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Balance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BalanceAmountController {
    private Connection connection;
    private App parent;

    private Balance balance;
    private List<BalanceAmount> data;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;

    public void provideApp(App parent, Connection connection, Balance balance) {
        this.connection = connection;
        this.parent = parent;
        this.balance = balance;
        data = new ArrayList<>();
        barChart.setAnimated(false);
        fillData();
        bind();
    }

    private void fillData() {
        data.clear();
        try {
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select o.credit_date, o.debit-o.credit amount from operations o where o.balance_id=" + balance.getId());
            while (rs.next()) {
                String date = rs.getDate(1).toString();
                int amount = rs.getInt(2);
                System.out.println("" + date + " " + amount);
                data.add(new BalanceAmount(date, amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    private void bind() {
        categoryAxis.setLabel("Dates");
        numberAxis.setLabel("Amount");
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach(balanceAmount -> series.getData().add(new XYChart.Data<>(balanceAmount.getDate(), Math.round(balanceAmount.getAmount()))));
        barChart.getData().addAll(series);
    }

    @FXML
    public void backClick() {
        parent.showBalancesWindow();
    }
}
