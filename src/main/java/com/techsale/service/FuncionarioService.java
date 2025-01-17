package com.techsale.service;

import com.techsale.database.DatabaseManager;
import com.techsale.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioService {

    public void cadastrarFuncionario(Funcionario f) {
        String sql = "INSERT INTO funcionario (nome, tipo, salario_base, porcentagem_comissao, horas_trabalhadas) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, f.getNome());
            pstmt.setInt(2, f.getTipo());
            pstmt.setDouble(3, f.getSalarioBase());
            pstmt.setDouble(4, f.getPorcentagemComissao());
            pstmt.setDouble(5, f.getHorasTrabalhadas());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Funcionario cadastrado: " + f.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void atualizarFuncionario(Funcionario f) {
        String sql = """
            UPDATE funcionario
            SET nome = ?, tipo = ?, salario_base = ?, porcentagem_comissao = ?, horas_trabalhadas = ?
            WHERE id = ?
            """;
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, f.getNome());
            pstmt.setInt(2, f.getTipo());
            pstmt.setDouble(3, f.getSalarioBase());
            pstmt.setDouble(4, f.getPorcentagemComissao());
            pstmt.setDouble(5, f.getHorasTrabalhadas());
            pstmt.setInt(6, f.getId());
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Funcionario atualizado: " + f.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void excluirFuncionario(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Funcionario excluido ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Funcionario buscarFuncionario(int id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTipo(rs.getInt("tipo"));
                f.setSalarioBase(rs.getDouble("salario_base"));
                f.setPorcentagemComissao(rs.getDouble("porcentagem_comissao"));
                f.setHorasTrabalhadas(rs.getDouble("horas_trabalhadas"));
                return f;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTipo(rs.getInt("tipo"));
                f.setSalarioBase(rs.getDouble("salario_base"));
                f.setPorcentagemComissao(rs.getDouble("porcentagem_comissao"));
                f.setHorasTrabalhadas(rs.getDouble("horas_trabalhadas"));
                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Agora o cálculo de salário utiliza os dados do Funcionario armazenados no BD
    // em vez de ser fixo ou "hardcoded".
    public double calcularSalario(int funcionarioId) {
        Funcionario f = buscarFuncionario(funcionarioId);
        if (f == null) {
            System.out.println("Funcionario nao encontrado. Retornando salario 0.");
            return 0.0;
        }
        // 1 - Assalariado (fixo)
        // 2 - Comissionado (porcentagem_comissao * vendas) => aqui poderíamos somar vendas do mês
        // 3 - Por hora (salarioBase = 0, usa horasTrabalhadas * 20.0 p.ex.)
        // 4 - Salario base + comissao
        // (para ser real, precisaríamos somar as vendas, mas aqui é simplificado)

        double salario = 0.0;
        switch (f.getTipo()) {
            case 1 -> {
                // Assalariado
                salario = f.getSalarioBase();
            }
            case 2 -> {
                // Comissionado
                // Vamos supor que a "salarioBase" seja 0 e a "porcentagem_comissao" seja 0.05 (5%)
                // e temos um valor de vendas fixo (ex.: 50k) ou calculado. Para simplificar, fixo:
                double vendas = 50000.0;
                salario = vendas * f.getPorcentagemComissao();
            }
            case 3 -> {
                // Por hora
                double valorHora = 20.0;
                salario = f.getHorasTrabalhadas() * valorHora;
            }
            case 4 -> {
                // Base + comissao
                // Vamos supor "salarioBase" e "porcentagem_comissao"
                double vendas = 40000.0;
                double comissao = vendas * f.getPorcentagemComissao();
                salario = f.getSalarioBase() + comissao;
            }
            default -> salario = 0.0;
        }
        return salario;
    }
}
