package com.techsale;

import com.techsale.database.DatabaseManager;
import com.techsale.model.*;
import com.techsale.model.Pedido.ItemPedido;
import com.techsale.service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final EletronicoService eletronicoService = new EletronicoService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final FuncionarioService funcionarioService = new FuncionarioService();

    public static void main(String[] args) {
        DatabaseManager.getConnection(); // inicializa DB e tabelas

        while (true) {
            System.out.println("\n=== Techsale: PDV de Eletronicos ===");
            System.out.println("1 - CRUD de Cliente");
            System.out.println("2 - CRUD de Eletronicos");
            System.out.println("3 - Realizar Pedido (Compra)");
            System.out.println("4 - Cancelar Pedido");
            System.out.println("5 - Emitir Fatura de Pedido");
            System.out.println("6 - CRUD de Funcionarios");
            System.out.println("7 - Calcular Salario do Funcionario");
            System.out.println("8 - Relatorio de Vendas por Cliente");
            System.out.println("9 - Sair");
            System.out.print("Selecione uma opcao: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> menuCliente();
                case "2" -> menuEletronicos();
                case "3" -> realizarPedido();
                case "4" -> cancelarPedido();
                case "5" -> emitirFaturaPedido();
                case "6" -> menuFuncionario();
                case "7" -> calcularSalario();
                case "8" -> relatorioVendasPorCliente();
                case "9" -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    /* ======================================================
     *                 MÉTODOS DE LEITURA SEGURA
     *  ====================================================== */
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Tente novamente.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Tente novamente.");
            }
        }
    }

    /* ======================================================
     *                MENU DE CLIENTE (CRUD)
     *  ====================================================== */
    private static void menuCliente() {
        while (true) {
            System.out.println("\n=== Menu Cliente ===");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Atualizar Cliente");
            System.out.println("3 - Excluir Cliente");
            System.out.println("4 - Consultar Cliente");
            System.out.println("5 - Voltar");
            String opc = scanner.nextLine();

            switch (opc) {
                case "1" -> cadastrarCliente();
                case "2" -> atualizarCliente();
                case "3" -> excluirCliente();
                case "4" -> consultarCliente();
                case "5" -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Endereco do cliente: ");
        String endereco = scanner.nextLine();
        Cliente cliente = new Cliente(nome, endereco);
        clienteService.cadastrarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void atualizarCliente() {
        int id = lerInteiro("Informe o ID do cliente: ");
        Cliente clienteExistente = clienteService.buscarCliente(id);
        if (clienteExistente == null) {
            System.out.println("Cliente nao encontrado.");
            return;
        }
        System.out.print("Novo nome (atual: " + clienteExistente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo endereco (atual: " + clienteExistente.getEndereco() + "): ");
        String novoEndereco = scanner.nextLine();

        clienteExistente.setNome(novoNome);
        clienteExistente.setEndereco(novoEndereco);
        clienteService.atualizarCliente(clienteExistente);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private static void excluirCliente() {
        int id = lerInteiro("Informe o ID do cliente: ");
        clienteService.excluirCliente(id);
        System.out.println("Cliente excluido com sucesso!");
    }

    private static void consultarCliente() {
        int id = lerInteiro("Informe o ID do cliente: ");
        Cliente cliente = clienteService.buscarCliente(id);
        if (cliente == null) {
            System.out.println("Cliente nao encontrado.");
        } else {
            System.out.println("ID: " + cliente.getId());
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("Endereco: " + cliente.getEndereco());
        }
    }

    /* ======================================================
     *           MENU DE ELETRONICOS (CRUD)
     *  ====================================================== */
    private static void menuEletronicos() {
        while (true) {
            System.out.println("\n=== Menu Eletronicos ===");
            System.out.println("1 - Cadastrar Eletronico");
            System.out.println("2 - Atualizar Eletronico");
            System.out.println("3 - Excluir Eletronico");
            System.out.println("4 - Consultar Eletronico");
            System.out.println("5 - Listar Eletronicos");
            System.out.println("6 - Voltar");
            String opc = scanner.nextLine();

            switch (opc) {
                case "1" -> cadastrarEletronico();
                case "2" -> atualizarEletronico();
                case "3" -> excluirEletronico();
                case "4" -> consultarEletronico();
                case "5" -> listarEletronicos();
                case "6" -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private static void cadastrarEletronico() {
        System.out.print("Nome do eletronico: ");
        String nome = scanner.nextLine();
        System.out.print("Marca do eletronico: ");
        String marca = scanner.nextLine();
        double preco = lerDouble("Preco do eletronico: ");
        int estoque = lerInteiro("Quantidade em estoque: ");
        Eletronico e = new Eletronico(nome, marca, preco, estoque);
        eletronicoService.cadastrarEletronico(e);
        System.out.println("Eletronico cadastrado com sucesso!");
    }

    private static void atualizarEletronico() {
        int id = lerInteiro("Informe o ID do eletronico: ");
        Eletronico existente = eletronicoService.buscarEletronico(id);
        if (existente == null) {
            System.out.println("Eletronico nao encontrado.");
            return;
        }
        System.out.print("Novo nome (atual: " + existente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        System.out.print("Nova marca (atual: " + existente.getMarca() + "): ");
        String novaMarca = scanner.nextLine();
        double novoPreco = lerDouble("Novo preco (atual: " + existente.getPreco() + "): ");
        int novoEstoque = lerInteiro("Novo estoque (atual: " + existente.getEstoque() + "): ");

        existente.setNome(novoNome);
        existente.setMarca(novaMarca);
        existente.setPreco(novoPreco);
        existente.setEstoque(novoEstoque);
        eletronicoService.atualizarEletronico(existente);
        System.out.println("Eletronico atualizado com sucesso!");
    }

    private static void excluirEletronico() {
        int id = lerInteiro("Informe o ID do eletronico: ");
        eletronicoService.excluirEletronico(id);
        System.out.println("Eletronico excluido com sucesso!");
    }

    private static void consultarEletronico() {
        int id = lerInteiro("Informe o ID do eletronico: ");
        Eletronico e = eletronicoService.buscarEletronico(id);
        if (e == null) {
            System.out.println("Eletronico nao encontrado.");
        } else {
            System.out.println("ID: " + e.getId());
            System.out.println("Nome: " + e.getNome());
            System.out.println("Marca: " + e.getMarca());
            System.out.println("Preco: " + e.getPreco());
            System.out.println("Estoque: " + e.getEstoque());
        }
    }

    private static void listarEletronicos() {
        List<Eletronico> eletronicos = eletronicoService.listarEletronicos();
        if (eletronicos.isEmpty()) {
            System.out.println("Nao ha eletronicos cadastrados.");
            return;
        }
        System.out.println("\n--- Lista de Eletronicos ---");
        for (Eletronico ele : eletronicos) {
            System.out.println("ID: " + ele.getId() + " | Nome: " + ele.getNome() +
                    " | Marca: " + ele.getMarca() +
                    " | Preco: " + ele.getPreco() +
                    " | Estoque: " + ele.getEstoque());
        }
    }

    /* ======================================================
     *                 REALIZAR PEDIDO
     *  ====================================================== */
    private static void realizarPedido() {
        System.out.println("\n=== Realizar Pedido ===");
        int clienteId = lerInteiro("ID do cliente (ou 0 para cadastrar novo): ");
        Cliente cliente = null;
        if (clienteId == 0) {
            System.out.println("Cadastrar novo cliente:");
            cadastrarCliente();
            // pegar o ultimo cliente inserido (simples)
            cliente = clienteService.buscarUltimoCliente();
        } else {
            cliente = clienteService.buscarCliente(clienteId);
            if (cliente == null) {
                System.out.println("Cliente nao encontrado. Operacao cancelada.");
                return;
            }
        }

        double limiteGasto = lerDouble("Quanto voce pode gastar nessa compra? ");

        List<Eletronico> eletronicos = eletronicoService.listarEletronicos();
        if (eletronicos.isEmpty()) {
            System.out.println("Nao ha eletronicos disponiveis para compra.");
            return;
        }

        double totalCompra = 0.0;
        Pedido pedido = new Pedido();
        pedido.setClienteId(cliente.getId());
        pedido.setEnderecoEntrega(cliente.getEndereco()); // simplificado
        pedido.setFaturaEmitida(false);

        while (true) {
            System.out.println("\n--- Catalogo de Eletronicos ---");
            for (Eletronico e : eletronicos) {
                System.out.println("ID: " + e.getId() + " | " + e.getNome() +
                        " | R$ " + e.getPreco() +
                        " | Estoque: " + e.getEstoque());
            }
            int idE = lerInteiro("Digite o ID do eletronico que deseja adicionar (0 para encerrar): ");
            if (idE == 0) break;

            Eletronico escolhido = eletronicoService.buscarEletronico(idE);
            if (escolhido == null) {
                System.out.println("Eletronico nao encontrado!");
            } else {
                if (escolhido.getEstoque() <= 0) {
                    System.out.println("Estoque insuficiente desse produto!");
                    continue;
                }
                int qtd = lerInteiro("Quantas unidades deseja adicionar? ");
                if (qtd <= 0) {
                    System.out.println("Quantidade invalida!");
                    continue;
                }
                if (qtd > escolhido.getEstoque()) {
                    System.out.println("Quantidade excede o estoque disponível!");
                    continue;
                }

                double valorParcial = escolhido.getPreco() * qtd;
                if ((totalCompra + valorParcial) > limiteGasto) {
                    System.out.println("Nao foi possivel adicionar. Limite de gasto excedido!");
                } else {
                    // Adiciona item
                    pedido.getItens().add(new ItemPedido(escolhido, qtd));
                    totalCompra += valorParcial;
                    System.out.println("Eletronico adicionado ao pedido!");
                }
            }
        }

        if (pedido.getItens().isEmpty()) {
            System.out.println("Nenhum item no pedido. Operacao cancelada.");
            return;
        }

        System.out.print("Selecione metodo de pagamento (1 - Dinheiro | 2 - Cartao de Credito): ");
        String metodoPagamento = scanner.nextLine();
        pedido.setFormaPagamento(metodoPagamento.equals("1") ? "Dinheiro" : "Cartao");

        pedido.setValorTotal(totalCompra);
        pedidoService.criarPedido(pedido);
        System.out.println("Pedido criado com sucesso! Valor total: R$ " + totalCompra);
    }

    private static void cancelarPedido() {
        System.out.println("\n=== Cancelar Pedido ===");
        int pedidoId = lerInteiro("Informe o ID do pedido: ");
        pedidoService.cancelarPedido(pedidoId);
    }

    private static void emitirFaturaPedido() {
        System.out.println("\n=== Emitir Fatura de Pedido ===");
        int pedidoId = lerInteiro("Informe o ID do pedido: ");
        pedidoService.emitirFatura(pedidoId);
    }

    /* ======================================================
     *              CRUD DE FUNCIONARIO
     *  ====================================================== */
    private static void menuFuncionario() {
        while (true) {
            System.out.println("\n=== Menu Funcionario ===");
            System.out.println("1 - Cadastrar Funcionario");
            System.out.println("2 - Atualizar Funcionario");
            System.out.println("3 - Excluir Funcionario");
            System.out.println("4 - Consultar Funcionario");
            System.out.println("5 - Listar Funcionarios");
            System.out.println("6 - Voltar");
            String opc = scanner.nextLine();

            switch (opc) {
                case "1" -> cadastrarFuncionario();
                case "2" -> atualizarFuncionario();
                case "3" -> excluirFuncionario();
                case "4" -> consultarFuncionario();
                case "5" -> listarFuncionarios();
                case "6" -> { return; }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private static void cadastrarFuncionario() {
        System.out.print("Nome do funcionario: ");
        String nome = scanner.nextLine();
        int tipo = lerInteiro("Tipo (1=Assalariado, 2=Comissionado, 3=Por Hora, 4=Base+Comissao): ");
        double salarioBase = lerDouble("Salario base (caso não use, deixar 0): ");
        double porcentagemComissao = lerDouble("Porcentagem de comissao (ex: 0.05 = 5%): ");
        double horasTrabalhadas = lerDouble("Horas trabalhadas (caso não use, deixar 0): ");

        Funcionario f = new Funcionario(nome, tipo, salarioBase, porcentagemComissao, horasTrabalhadas);
        funcionarioService.cadastrarFuncionario(f);
        System.out.println("Funcionario cadastrado com sucesso!");
    }

    private static void atualizarFuncionario() {
        int id = lerInteiro("Informe o ID do funcionario: ");
        Funcionario fExistente = funcionarioService.buscarFuncionario(id);
        if (fExistente == null) {
            System.out.println("Funcionario nao encontrado.");
            return;
        }
        System.out.print("Novo nome (atual: " + fExistente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        int novoTipo = lerInteiro("Novo tipo (atual: " + fExistente.getTipo() + "): ");
        double novoSalarioBase = lerDouble("Novo salario base (atual: " + fExistente.getSalarioBase() + "): ");
        double novaComissao = lerDouble("Nova porcentagemComissao (atual: " + fExistente.getPorcentagemComissao() + "): ");
        double novasHoras = lerDouble("Nova horasTrabalhadas (atual: " + fExistente.getHorasTrabalhadas() + "): ");

        fExistente.setNome(novoNome);
        fExistente.setTipo(novoTipo);
        fExistente.setSalarioBase(novoSalarioBase);
        fExistente.setPorcentagemComissao(novaComissao);
        fExistente.setHorasTrabalhadas(novasHoras);

        funcionarioService.atualizarFuncionario(fExistente);
        System.out.println("Funcionario atualizado com sucesso!");
    }

    private static void excluirFuncionario() {
        int id = lerInteiro("Informe o ID do funcionario: ");
        funcionarioService.excluirFuncionario(id);
        System.out.println("Funcionario excluido com sucesso!");
    }

    private static void consultarFuncionario() {
        int id = lerInteiro("Informe o ID do funcionario: ");
        Funcionario f = funcionarioService.buscarFuncionario(id);
        if (f == null) {
            System.out.println("Funcionario nao encontrado.");
            return;
        }
        System.out.println("ID: " + f.getId());
        System.out.println("Nome: " + f.getNome());
        System.out.println("Tipo: " + f.getTipo());
        System.out.println("SalarioBase: " + f.getSalarioBase());
        System.out.println("PorcentagemComissao: " + f.getPorcentagemComissao());
        System.out.println("HorasTrabalhadas: " + f.getHorasTrabalhadas());
    }

    private static void listarFuncionarios() {
        List<Funcionario> lista = funcionarioService.listarFuncionarios();
        if (lista.isEmpty()) {
            System.out.println("Nao ha funcionarios cadastrados.");
            return;
        }
        for (Funcionario ff : lista) {
            System.out.println("ID: " + ff.getId() +
                    " | Nome: " + ff.getNome() +
                    " | Tipo: " + ff.getTipo() +
                    " | SalarioBase: " + ff.getSalarioBase() +
                    " | %Comissao: " + ff.getPorcentagemComissao() +
                    " | HorasTrabalhadas: " + ff.getHorasTrabalhadas());
        }
    }

    /* ======================================================
     *           CALCULAR SALARIO (FUNCIONARIO)
     *  ====================================================== */
    private static void calcularSalario() {
        System.out.println("\n=== Calcular Salario do Funcionario ===");
        int funcId = lerInteiro("Informe o ID do funcionario: ");
        double salario = funcionarioService.calcularSalario(funcId);
        System.out.println("Salario calculado: R$ " + salario);
    }

    /* ======================================================
     *         RELATORIO DE VENDAS POR CLIENTE
     *  ====================================================== */
    private static void relatorioVendasPorCliente() {
        int clienteId = lerInteiro("\nInforme o ID do cliente para gerar relatorio: ");
        System.out.println("Deseja filtrar por data? (s/n) ");
        String resp = scanner.nextLine();
        List<Pedido> pedidos;

        if (resp.equalsIgnoreCase("s")) {
            System.out.println("Formato esperado: yyyy-MM-dd HH:mm:ss");
            System.out.print("Data inicial: ");
            String dataIni = scanner.nextLine();
            System.out.print("Data final: ");
            String dataFim = scanner.nextLine();
            pedidos = pedidoService.listarPedidosPorClienteEData(clienteId, dataIni, dataFim);
        } else {
            pedidos = pedidoService.listarPedidosPorCliente(clienteId);
        }

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este filtro.");
            return;
        }

        double totalGasto = 0.0;
        System.out.println("\n=== Relatorio de Vendas ===");
        for (Pedido p : pedidos) {
            System.out.println("Pedido ID: " + p.getId());
            System.out.println("Data Emissao: " + p.getDataEmissao());
            System.out.println("Status: " + (p.isCancelado() ? "Cancelado" : "Ativo"));
            System.out.println("Fatura Emitida: " + (p.isFaturaEmitida() ? "Sim" : "Não"));
            System.out.println("Itens:");
            for (ItemPedido item : p.getItens()) {
                System.out.println("  -> " + item.getEletronico().getNome() +
                        " (R$ " + item.getEletronico().getPreco() +
                        ") x " + item.getQuantidade());
            }
            System.out.println("Valor Total do Pedido: R$ " + p.getValorTotal());
            System.out.println("-------------------------------------");
            totalGasto += p.getValorTotal();
        }
        System.out.println("Valor total gasto pelo cliente: R$ " + totalGasto);
    }
}
