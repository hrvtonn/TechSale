package com.techsale.service;

import com.techsale.database.DatabaseManager;
import com.techsale.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    public void cadastrarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, endereco) VALUES (?, ?)";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEndereco());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Cliente cadastrado: " + cliente.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void atualizarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ? WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEndereco());
            pstmt.setInt(3, cliente.getId());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Cliente atualizado: " + cliente.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void excluirCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Cliente excluido ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Cliente buscarCliente(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cliente buscarUltimoCliente() {
        String sql = "SELECT * FROM cliente ORDER BY id DESC LIMIT 1";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                clientes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}
