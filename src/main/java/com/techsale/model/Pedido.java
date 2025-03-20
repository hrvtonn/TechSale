package com.techsale.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private int clienteId;
    private Date dataEmissao;
    private String enderecoEntrega;
    private String formaPagamento;
    private double valorTotal;
    private boolean cancelado;
    private boolean faturaEmitida;

    // Agora vamos guardar itens + quantidades. Em vez de um simples List<Eletronico>,
    // podemos mapear cada Eletronico a uma quantidade.
    // Para simplicidade, podemos criar uma classe auxiliar ou usar um Map<Eletronico, Integer>.
    // Faremos uma classe "ItemPedido" simples no mesmo arquivo ou fora.

    private List<ItemPedido> itens;

    public Pedido() {
        this.dataEmissao = new Date();
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getClienteId() {
        return clienteId;
    }
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    public Date getDataEmissao() {
        return dataEmissao;
    }
    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }
    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
    public String getFormaPagamento() {
        return formaPagamento;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public boolean isCancelado() {
        return cancelado;
    }
    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }
    public boolean isFaturaEmitida() {
        return faturaEmitida;
    }
    public void setFaturaEmitida(boolean faturaEmitida) {
        this.faturaEmitida = faturaEmitida;
    }
    public List<ItemPedido> getItens() {
        return itens;
    }
    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    // Classe auxiliar para manter a quantidade de cada Eletronico
    public static class ItemPedido {
        private Eletronico eletronico;
        private int quantidade;

        public ItemPedido(Eletronico ele, int qtd) {
            this.eletronico = ele;
            this.quantidade = qtd;
        }

        public Eletronico getEletronico() {
            return eletronico;
        }
        public void setEletronico(Eletronico eletronico) {
            this.eletronico = eletronico;
        }
        public int getQuantidade() {
            return quantidade;
        }
        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }
}
