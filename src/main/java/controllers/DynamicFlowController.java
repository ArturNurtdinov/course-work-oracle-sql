package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Article;
import model.SimpleOperation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicFlowController {
    private DynamicSettingsController parent;
    private Connection connection;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<Article> articles;
    private Map<String, Integer> data;

    @FXML
    private LineChart<String, Number> chart;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;

    public void provideApp(DynamicSettingsController parent, Connection connection, LocalDate fromDate, LocalDate toDate, List<Article> articles, String type) {
        this.parent = parent;
        this.connection = connection;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.articles = articles;
        chart.setAnimated(false);
        categoryAxis.setLabel("Date");
        numberAxis.setLabel(type);
        data = new HashMap<>();

        if (type.equalsIgnoreCase("debit")) {
            fillDebitData();
            showDebits();
        } else if (type.equalsIgnoreCase("credit")) {
            fillCreditData();
            showCredits();
        } else {
            throw new IllegalStateException("Unknown type!");
        }
    }

    private void fillDebitData() {
        List<SimpleOperation> operations = new ArrayList<>();
        try {
            List<String> articleIds = articles.stream()
                    .map(article -> "" + article.getId())
                    .collect(Collectors.toList());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from OPERATIONS o where o.article_id in (" + String.join(",", articleIds) +
                    ") and o.credit_date between to_date('" + Date.valueOf(fromDate) + "', 'yy.MM.DD') and to_date('" + Date.valueOf(toDate) + "', 'yy.MM.DD') ");
            while (rs.next()) {
                operations.add(new SimpleOperation(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),
                        rs.getDate(5), rs.getInt(6)));
            }

            operations.forEach(operation -> {
                data.put(operation.getCreditDate().toString(), data.getOrDefault(operation.getCreditDate().toString(), 0) + operation.getDebit());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillCreditData() {
        List<SimpleOperation> operations = new ArrayList<>();
        try {
            List<String> articleIds = articles.stream()
                    .map(article -> "" + article.getId())
                    .collect(Collectors.toList());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from OPERATIONS o where o.article_id in (" + String.join(",", articleIds) +
                    ") and o.credit_date between to_date('" + Date.valueOf(fromDate) + "', 'yy.MM.DD') and to_date('" + Date.valueOf(toDate) + "', 'yy.MM.DD') ");
            while (rs.next()) {
                operations.add(new SimpleOperation(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),
                        rs.getDate(5), rs.getInt(6)));
            }

            operations.forEach(operation -> {
                data.put(operation.getCreditDate().toString(), data.getOrDefault(operation.getCreditDate().toString(), 0) + operation.getCredit());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDebits() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));
        chart.getData().addAll(series);
    }

    private void showCredits() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));
        chart.getData().addAll(series);
    }
}
