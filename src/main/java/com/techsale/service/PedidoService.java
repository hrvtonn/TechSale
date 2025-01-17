package com.techsale.service;

import com.techsale.database.DatabaseManager;
import com.techsale.model.Eletronico;
import com.techsale.model.Pedido;
import com.techsale.model.Pedido.ItemPedido;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    public void criarPedido(Pedido pedido) {
        // Vamos inserir o Pedido e seus itens em uma transação
        Connection conn = DatabaseManager.getConnection();
        try {
            String insertPedido = """
                INSERT INTO pedido (cliente_id, data_emissao, endereco_entrega, forma_pagamento, 
                                    valor_total, cancelado, fatura_emitida)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, pedido.getClienteId());
                pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pedido.getDataEmissao()));
                pstmt.setString(3, pedido.getEnderecoEntrega());
                pstmt.setString(4, pedido.getFormaPagamento());
                pstmt.setDouble(5, pedido.getValorTotal());
                pstmt.setInt(6, pedido.isCancelado() ? 1 : 0);
                pstmt.setInt(7, pedido.isFaturaEmitida() ? 1 : 0);
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getInt(1));
                }
            }

            // Agora inserir itens
            String insertItem = "INSERT INTO pedido_itens (pedido_id, eletronico_id, quantidade) VALUES (?, ?, ?)";
            for (ItemPedido ip : pedido.getItens()) {
                try (PreparedStatement itemStmt = conn.prepareStatement(insertItem)) {
                    itemStmt.setInt(1, pedido.getId());
                    itemStmt.setInt(2, ip.getEletronico().getId());
                    itemStmt.setInt(3, ip.getQuantidade());
                    itemStmt.executeUpdate();
                }
                // Decrementar estoque
                String updateEstoque = "UPDATE eletronico SET estoque = estoque - ? WHERE id = ?";
                try (PreparedStatement updStmt = conn.prepareStatement(updateEstoque)) {
                    updStmt.setInt(1, ip.getQuantidade());
                    updStmt.setInt(2, ip.getEletronico().getId());
                    updStmt.executeUpdate();
                }
            }
            conn.commit();
            System.out.println("[LOG] Pedido criado com sucesso. ID: " + pedido.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void cancelarPedido(int pedidoId) {
        Pedido p = buscarPedido(pedidoId);
        if (p == null) {
            System.out.println("Pedido nao encontrado.");
            return;
        }
        if (p.isFaturaEmitida()) {
            System.out.println("Nao foi possivel cancelar. Ja existe fatura emitida.");
            return;
        }
        // Cancelar
        String sql = "UPDATE pedido SET cancelado = 1 WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("Pedido cancelado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void emitirFatura(int pedidoId) {
        // Marca fatura_emitida = 1 (caso esteja tudo certo)
        // Se pedido já estiver cancelado, não faz sentido emitir
        Pedido p = buscarPedido(pedidoId);
        if (p == null) {
            System.out.println("Pedido nao encontrado.");
            return;
        }
        if (p.isCancelado()) {
            System.out.println("Pedido cancelado, não é possível emitir fatura.");
            return;
        }
        String sql = "UPDATE pedido SET fatura_emitida = 1 WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            pstmt.executeUpdate();
            DatabaseManager.getConnection().commit();
            System.out.println("[LOG] Fatura emitida para o pedido ID: " + pedidoId);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseManager.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Pedido buscarPedido(int pedidoId) {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setClienteId(rs.getInt("cliente_id"));
                p.setEnderecoEntrega(rs.getString("endereco_entrega"));
                p.setFormaPagamento(rs.getString("forma_pagamento"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setCancelado(rs.getInt("cancelado") == 1);
                p.setFaturaEmitida(rs.getInt("fatura_emitida") == 1);
                // data emissao -> sem parse
                // p.setDataEmissao(...);

                p.setItens(buscarItensPedido(pedidoId));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ItemPedido> buscarItensPedido(int pedidoId) {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = """
            SELECT e.id, e.nome, e.marca, e.preco, e.estoque, pi.quantidade
            FROM pedido_itens pi
            JOIN eletronico e ON pi.eletronico_id = e.id
            WHERE pi.pedido_id = ?
            """;
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Eletronico ele = new Eletronico();
                ele.setId(rs.getInt("id"));
                ele.setNome(rs.getString("nome"));
                ele.setMarca(rs.getString("marca"));
                ele.setPreco(rs.getDouble("preco"));
                ele.setEstoque(rs.getInt("estoque"));

                int qtd = rs.getInt("quantidade");
                itens.add(new ItemPedido(ele, qtd));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }

    public List<Pedido> listarPedidosPorCliente(int clienteId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE cliente_id = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setClienteId(rs.getInt("cliente_id"));
                p.setEnderecoEntrega(rs.getString("endereco_entrega"));
                p.setFormaPagamento(rs.getString("forma_pagamento"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setCancelado(rs.getInt("cancelado") == 1);
                p.setFaturaEmitida(rs.getInt("fatura_emitida") == 1);

                p.setItens(buscarItensPedido(p.getId()));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Exemplo de listagem por período
    public List<Pedido> listarPedidosPorClienteEData(int clienteId, String dataInicial, String dataFinal) {
        // data_emissao no formato "yyyy-MM-dd HH:mm:ss"
        List<Pedido> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM pedido
            WHERE cliente_id = ?
              AND data_emissao >= ?
              AND data_emissao <= ?
        """;
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setString(2, dataInicial);
            pstmt.setString(3, dataFinal);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setClienteId(rs.getInt("cliente_id"));
                p.setEnderecoEntrega(rs.getString("endereco_entrega"));
                p.setFormaPagamento(rs.getString("forma_pagamento"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setCancelado(rs.getInt("cancelado") == 1);
                p.setFaturaEmitida(rs.getInt("fatura_emitida") == 1);

                p.setItens(buscarItensPedido(p.getId()));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
