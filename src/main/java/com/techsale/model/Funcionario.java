package com.techsale.model;

public class Funcionario {
    private int id;
    private String nome;
    private int tipo; // 1=Assalariado, 2=Comissionado, 3=Por Hora, 4=Base+Comissao
    private double salarioBase;
    private double porcentagemComissao;
    private double horasTrabalhadas;

    public Funcionario() {
    }

    public Funcionario(String nome, int tipo, double salarioBase, double porcentagemComissao, double horasTrabalhadas) {
        this.nome = nome;
        this.tipo = tipo;
        this.salarioBase = salarioBase;
        this.porcentagemComissao = porcentagemComissao;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getTipo() {
        return tipo;
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public double getSalarioBase() {
        return salarioBase;
    }
    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }
    public double getPorcentagemComissao() {
        return porcentagemComissao;
    }
    public void setPorcentagemComissao(double porcentagemComissao) {
        this.porcentagemComissao = porcentagemComissao;
    }
    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }
    public void setHorasTrabalhadas(double horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }
}
