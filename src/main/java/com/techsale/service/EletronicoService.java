package com.techsale.service;

import com.techsale.database.DatabaseManager;
import com.techsale.model.Eletronico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EletronicoService {

    public void cadastrarEletronico(Eletronico ele) {
        String sql = "INSERT INTO eletronico (nome, marca, preco, estoque) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, ele.getNome());
            pstmt.setString(2, ele.getMarca());
            pstmt.setDouble(3, ele.getPreco());
            pstmt.setInt(4, ele.getEstoque());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Eletronico cadastrado: " + ele.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void atualizarEletronico(Eletronico ele) {
        String sql = "UPDATE eletronico SET nome = ?, marca = ?, preco = ?, estoque = ? WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, ele.getNome());
            pstmt.setString(2, ele.getMarca());
            pstmt.setDouble(3, ele.getPreco());
            pstmt.setInt(4, ele.getEstoque());
            pstmt.setInt(5, ele.getId());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Eletronico atualizado: " + ele.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void excluirEletronico(int id) {
        String sql = "DELETE FROM eletronico WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Eletronico excluido ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Eletronico buscarEletronico(int id) {
        String sql = "SELECT * FROM eletronico WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Eletronico e = new Eletronico();
                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setMarca(rs.getString("marca"));
                e.setPreco(rs.getDouble("preco"));
                e.setEstoque(rs.getInt("estoque"));
                return e;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Eletronico> listarEletronicos() {
        List<Eletronico> lista = new ArrayList<>();
        String sql = "SELECT * FROM eletronico";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Eletronico e = new Eletronico();
                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setMarca(rs.getString("marca"));
                e.setPreco(rs.getDouble("preco"));
                e.setEstoque(rs.getInt("estoque"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
