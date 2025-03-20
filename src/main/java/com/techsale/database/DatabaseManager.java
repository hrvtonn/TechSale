package com.techsale.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:techsale.db";
    private static Connection connection;

    static {
        try {
            // Carregar o driver SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false); // Desativar o auto-commit para transações manuais
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
