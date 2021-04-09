package controllers;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import model.Article;
import model.FinancialFlow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FinancialFlowController {
    private Connection connection;
    private App parent;

    private List<FinancialFlow<Article>> data;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;

    public void provideApp(App parent, Connection connection) {
        this.connection = connection;
        this.parent = parent;
        data = new ArrayList<>();
        barChart.setAnimated(false);
        fillDebitData();
        bindDebit();
    }

    private void fillDebitData() {
        data.clear();
        try {
            List<Article> articles = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2)));
            }

            List<Pair<Article, Integer>> articleWithTotalDebit = new ArrayList<>();
            articles.forEach(article -> {
                try {
                    ResultSet rs2 = connection.createStatement().executeQuery("select sum(o.debit), a.id from articles a join operations o on o.article_id = a.id where a.id = "
                            + article.getId() + " group by a.id");
                    if (rs2.next()) {
                        articleWithTotalDebit.add(new Pair<>(article, rs2.getInt(1)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    parent.showErrorAlert(e.getMessage());
                }
            });
            int total = -1;
            rs = connection.createStatement().executeQuery("select sum(o.debit) from operations o");
            if (rs.next()) {
                total = rs.getInt(1);
            }

            int finalTotal = total;
            articleWithTotalDebit.forEach(pair -> data.add(new FinancialFlow<>(pair.getKey(), pair.getValue() / finalTotal * 100)));
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    private void fillCreditData() {
        data.clear();
        try {
            List<Article> articles = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from articles");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2)));
            }

            List<Pair<Article, Double>> articleWithTotalCredit = new ArrayList<>();
            articles.forEach(article -> {
                try {
                    ResultSet rs2 = connection.createStatement().executeQuery("select sum(o.credit), a.id from articles a join operations o on o.article_id = a.id where a.id = "
                            + article.getId() + " group by a.id");
                    if (rs2.next()) {
                        int res = rs2.getInt(1);
                        articleWithTotalCredit.add(new Pair<>(article, (double) res));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    parent.showErrorAlert(e.getMessage());
                }
            });
            int total = -1;
            rs = connection.createStatement().executeQuery("select sum(o.credit) from operations o");
            if (rs.next()) {
                total = rs.getInt(1);
            }

            int finalTotal = total;
            articleWithTotalCredit.forEach(pair -> data.add(new FinancialFlow<>(pair.getKey(), pair.getValue() / finalTotal * 100)));
        } catch (Exception e) {
            e.printStackTrace();
            parent.showErrorAlert(e.getMessage());
        }
    }

    private void bindDebit() {
        categoryAxis.setLabel("Articles");
        numberAxis.setLabel("Percent of debit");
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach(flow -> series.getData().add(new XYChart.Data<>(flow.getFeature().getName(), Math.round(flow.getPercent()))));
        barChart.getData().addAll(series);
    }

    private void bindCredit() {
        categoryAxis.setLabel("Articles");
        numberAxis.setLabel("Percent of credit");
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach(flow -> series.getData().add(new XYChart.Data<>(flow.getFeature().getName(), Math.round(flow.getPercent()))));
        barChart.getData().addAll(series);
    }

    @FXML
    public void debitClick() {
        fillDebitData();
        bindDebit();
    }

    @FXML
    public void creditClick() {
        fillCreditData();
        bindCredit();
    }

    @FXML
    public void backClick() {
        parent.showMainWindow();
    }
}
