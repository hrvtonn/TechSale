package com.techsale;

import com.techsale.model.*;
import com.techsale.service.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TechSaleGUI extends JFrame {
    private ClienteService clienteService = new ClienteService();
    private EletronicoService eletronicoService = new EletronicoService();
    private PedidoService pedidoService = new PedidoService();
    private FuncionarioService funcionarioService = new FuncionarioService();

    public TechSaleGUI() {
        setTitle("Techsale: PDV de Eletronicos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Painel principal com layout de grade
        JPanel mainPanel = new JPanel(new GridLayout(9, 1, 10, 10));

        // Botões do menu
        JButton btnCliente = new JButton("1 - CRUD de Cliente");
        JButton btnEletronicos = new JButton("2 - CRUD de Eletronicos");
        JButton btnRealizarPedido = new JButton("3 - Realizar Pedido (Compra)");
        JButton btnCancelarPedido = new JButton("4 - Cancelar Pedido");
        JButton btnEmitirFatura = new JButton("5 - Emitir Fatura de Pedido");
        JButton btnFuncionarios = new JButton("6 - CRUD de Funcionarios");
        JButton btnCalcularSalario = new JButton("7 - Calcular Salario do Funcionario");
        JButton btnRelatorioVendas = new JButton("8 - Relatorio de Vendas por Cliente");
        JButton btnSair = new JButton("9 - Sair");

        // Adiciona os botões ao painel principal
        mainPanel.add(btnCliente);
        mainPanel.add(btnEletronicos);
        mainPanel.add(btnRealizarPedido);
        mainPanel.add(btnCancelarPedido);
        mainPanel.add(btnEmitirFatura);
        mainPanel.add(btnFuncionarios);
        mainPanel.add(btnCalcularSalario);
        mainPanel.add(btnRelatorioVendas);
        mainPanel.add(btnSair);

        // Adiciona o painel principal à janela
        add(mainPanel);

        // Listeners para os botões
        btnCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCRUDCliente();
            }
        });

        btnEletronicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCRUDEletronicos();
            }
        });

        btnRealizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaRealizarPedido();
            }
        });

        btnCancelarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCancelarPedido();
            }
        });

        btnEmitirFatura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaEmitirFatura();
            }
        });

        btnFuncionarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCRUDFuncionarios();
            }
        });

        btnCalcularSalario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCalcularSalario();
            }
        });

        btnRelatorioVendas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaRelatorioVendas();
            }
        });

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha a aplicação
            }
        });
    }

    // Métodos para abrir janelas específicas
    private void abrirJanelaCRUDCliente() {
        JFrame janelaCliente = new JFrame("CRUD de Cliente");
        janelaCliente.setSize(400, 300);
        janelaCliente.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField txtNome = new JTextField();
        JTextField txtEndereco = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Endereco:"));
        panel.add(txtEndereco);
        panel.add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String endereco = txtEndereco.getText();
                Cliente cliente = new Cliente(nome, endereco);
                clienteService.cadastrarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
            }
        });

        janelaCliente.add(panel);
        janelaCliente.setVisible(true);
    }

    private void abrirJanelaCRUDEletronicos() {
        JFrame janelaEletronicos = new JFrame("CRUD de Eletronicos");
        janelaEletronicos.setSize(400, 300);
        janelaEletronicos.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField txtNome = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtEstoque = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Marca:"));
        panel.add(txtMarca);
        panel.add(new JLabel("Preco:"));
        panel.add(txtPreco);
        panel.add(new JLabel("Estoque:"));
        panel.add(txtEstoque);
        panel.add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String marca = txtMarca.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());
                Eletronico eletronico = new Eletronico(nome, marca, preco, estoque);
                eletronicoService.cadastrarEletronico(eletronico);
                JOptionPane.showMessageDialog(null, "Eletronico cadastrado com sucesso!");
            }
        });

        janelaEletronicos.add(panel);
        janelaEletronicos.setVisible(true);
    }

    private void abrirJanelaRealizarPedido() {
        JFrame janelaPedido = new JFrame("Realizar Pedido");
        janelaPedido.setSize(400, 300);
        janelaPedido.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField txtClienteId = new JTextField();
        JTextField txtEletronicoId = new JTextField();
        JTextField txtQuantidade = new JTextField();
        JButton btnAdicionar = new JButton("Adicionar");

        panel.add(new JLabel("ID do Cliente:"));
        panel.add(txtClienteId);
        panel.add(new JLabel("ID do Eletronico:"));
        panel.add(txtEletronicoId);
        panel.add(new JLabel("Quantidade:"));
        panel.add(txtQuantidade);
        panel.add(btnAdicionar);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clienteId = Integer.parseInt(txtClienteId.getText());
                int eletronicoId = Integer.parseInt(txtEletronicoId.getText());
                int quantidade = Integer.parseInt(txtQuantidade.getText());

                Cliente cliente = clienteService.buscarCliente(clienteId);
                Eletronico eletronico = eletronicoService.buscarEletronico(eletronicoId);

                if (cliente != null && eletronico != null) {
                    Pedido pedido = new Pedido();
                    pedido.setClienteId(clienteId);
                    pedido.getItens().add(new Pedido.ItemPedido(eletronico, quantidade));
                    pedidoService.criarPedido(pedido);
                    JOptionPane.showMessageDialog(null, "Pedido criado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente ou Eletronico nao encontrado.");
                }
            }
        });

        janelaPedido.add(panel);
        janelaPedido.setVisible(true);
    }

    private void abrirJanelaCancelarPedido() {
        String pedidoId = JOptionPane.showInputDialog("Informe o ID do pedido:");
        if (pedidoId != null && !pedidoId.isEmpty()) {
            int id = Integer.parseInt(pedidoId);
            pedidoService.cancelarPedido(id);
            JOptionPane.showMessageDialog(null, "Pedido cancelado com sucesso!");
        }
    }

    private void abrirJanelaEmitirFatura() {
        String pedidoId = JOptionPane.showInputDialog("Informe o ID do pedido:");
        if (pedidoId != null && !pedidoId.isEmpty()) {
            int id = Integer.parseInt(pedidoId);
            pedidoService.emitirFatura(id);
            JOptionPane.showMessageDialog(null, "Fatura emitida com sucesso!");
        }
    }

    private void abrirJanelaCRUDFuncionarios() {
        JFrame janelaFuncionarios = new JFrame("CRUD de Funcionarios");
        janelaFuncionarios.setSize(400, 300);
        janelaFuncionarios.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField txtNome = new JTextField();
        JTextField txtTipo = new JTextField();
        JTextField txtSalarioBase = new JTextField();
        JTextField txtComissao = new JTextField();
        JTextField txtHorasTrabalhadas = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Tipo (1=Assalariado, 2=Comissionado, 3=Por Hora, 4=Base+Comissao):"));
        panel.add(txtTipo);
        panel.add(new JLabel("Salario Base:"));
        panel.add(txtSalarioBase);
        panel.add(new JLabel("Porcentagem de Comissao:"));
        panel.add(txtComissao);
        panel.add(new JLabel("Horas Trabalhadas:"));
        panel.add(txtHorasTrabalhadas);
        panel.add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                int tipo = Integer.parseInt(txtTipo.getText());
                double salarioBase = Double.parseDouble(txtSalarioBase.getText());
                double comissao = Double.parseDouble(txtComissao.getText());
                double horasTrabalhadas = Double.parseDouble(txtHorasTrabalhadas.getText());

                Funcionario funcionario = new Funcionario(nome, tipo, salarioBase, comissao, horasTrabalhadas);
                funcionarioService.cadastrarFuncionario(funcionario);
                JOptionPane.showMessageDialog(null, "Funcionario cadastrado com sucesso!");
            }
        });

        janelaFuncionarios.add(panel);
        janelaFuncionarios.setVisible(true);
    }

    private void abrirJanelaCalcularSalario() {
        String funcionarioId = JOptionPane.showInputDialog("Informe o ID do funcionario:");
        if (funcionarioId != null && !funcionarioId.isEmpty()) {
            int id = Integer.parseInt(funcionarioId);
            double salario = funcionarioService.calcularSalario(id);
            JOptionPane.showMessageDialog(null, "Salario calculado: R$ " + salario);
        }
    }

    private void abrirJanelaRelatorioVendas() {
        String clienteId = JOptionPane.showInputDialog("Informe o ID do cliente:");
        if (clienteId != null && !clienteId.isEmpty()) {
            int id = Integer.parseInt(clienteId);
            List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(id);

            StringBuilder relatorio = new StringBuilder();
            for (Pedido pedido : pedidos) {
                relatorio.append("Pedido ID: ").append(pedido.getId()).append("\n");
                relatorio.append("Valor Total: R$ ").append(pedido.getValorTotal()).append("\n");
                relatorio.append("Itens:\n");
                for (Pedido.ItemPedido item : pedido.getItens()) {
                    relatorio.append("  -> ").append(item.getEletronico().getNome())
                            .append(" x ").append(item.getQuantidade()).append("\n");
                }
                relatorio.append("----------------------------\n");
            }

            JOptionPane.showMessageDialog(null, relatorio.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TechSaleGUI().setVisible(true);
            }
        });
    }
}