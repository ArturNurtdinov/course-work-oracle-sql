package controllers;

import app.App;

import java.sql.Connection;

public class BalancesController {
    private App parent;
    private Connection connection;


    public void provideApp(App parent, Connection connection) {
        this.parent = parent;
        this.connection = connection;
    }
}
