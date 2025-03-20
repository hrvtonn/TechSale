package com.techsale.model;

public class Equipamento {
    private int id;
    private String descricao;

    public Equipamento() {
    }

    public Equipamento(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
