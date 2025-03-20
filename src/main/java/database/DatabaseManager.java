package com.techsale.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = "jdbc:sqlite:techsale.db";
                connection = DriverManager.getConnection(url);
                // Desligar autocommit para podermos usar transações
                connection.setAutoCommit(false);
                criarTabelas();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    private static void criarTabelas() {
        try (Statement stmt = connection.createStatement()) {
            // Tabela Cliente
            String clienteTable = """
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    endereco TEXT
                )
            """;
            stmt.execute(clienteTable);

            // Tabela Eletronico (agora com coluna estoque)
            String eletronicoTable = """
                CREATE TABLE IF NOT EXISTS eletronico (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    marca TEXT NOT NULL,
                    preco REAL NOT NULL,
                    estoque INTEGER DEFAULT 0
                )
            """;
            stmt.execute(eletronicoTable);

            // Tabela Pedido
            String pedidoTable = """
                CREATE TABLE IF NOT EXISTS pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER,
                    data_emissao TEXT,
                    endereco_entrega TEXT,
                    forma_pagamento TEXT,
                    valor_total REAL,
                    cancelado INTEGER DEFAULT 0,
                    fatura_emitida INTEGER DEFAULT 0,
                    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
                )
            """;
            stmt.execute(pedidoTable);

            // Tabela de relacionamento pedido_itens (agora com coluna quantidade)
            String pedidoItensTable = """
                CREATE TABLE IF NOT EXISTS pedido_itens (
                    pedido_id INTEGER,
                    eletronico_id INTEGER,
                    quantidade INTEGER DEFAULT 1,
                    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                    FOREIGN KEY (eletronico_id) REFERENCES eletronico(id)
                )
            """;
            stmt.execute(pedidoItensTable);

            // Tabela Funcionario
            String funcionarioTable = """
                CREATE TABLE IF NOT EXISTS funcionario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    tipo INTEGER NOT NULL,           -- 1=Assalariado, 2=Comissionado, 3=Por Hora, 4=Base+Comissao
                    salario_base REAL DEFAULT 0.0,
                    porcentagem_comissao REAL DEFAULT 0.0,
                    horas_trabalhadas REAL DEFAULT 0.0
                )
            """;
            stmt.execute(funcionarioTable);

            // Se chegou até aqui, confirma a transação de criação de tabelas
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
