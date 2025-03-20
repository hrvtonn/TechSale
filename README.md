# Techsale: PDV de Eletrônicos

Bem-vindo(a) ao **Techsale**, um sistema de Ponto de Venda (PDV) para produtos eletrônicos.  
Neste projeto, é possível realizar:

- **CRUD de Clientes** (cadastrar, atualizar, excluir e consultar).
- **CRUD de Eletrônicos** (cadastrar, atualizar, excluir e consultar).
- **CRUD de Funcionários** com cálculo de salário.
- **Realizar Pedidos**, definindo quantidade de itens, forma de pagamento, etc.
- **Cancelar Pedidos** (desde que não haja fatura emitida).
- **Emitir Fatura** para pedidos.
- **Gerar Relatório de Vendas** por cliente, com a possibilidade de filtrar por intervalo de datas.

---

## 1. Tecnologias Utilizadas

- **Java 21**
- **Maven** (gerenciador de dependências)
- **SQLite** (banco de dados local, sem necessidade de servidor externo)

---

## 2. Estrutura de Diretórios

```
Techsale
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- com.techsale
|   |   |       |-- Main.java               # Classe principal
|   |   |       |-- database
|   |   |       |   |-- DatabaseManager.java
|   |   |       |-- model
|   |   |       |   |-- Cliente.java
|   |   |       |   |-- Eletronico.java
|   |   |       |   |-- Pedido.java
|   |   |       |   |-- Equipamento.java
|   |   |       |   |-- Funcionario.java
|   |   |       |-- service
|   |   |       |   |-- ClienteService.java
|   |   |       |   |-- EletronicoService.java
|   |   |       |   |-- FuncionarioService.java
|   |   |       |   |-- PedidoService.java
|   |-- resources
|   |   |-- application.properties
|-- pom.xml
|-- README.md
```

- **Main.java**: Ponto de entrada do sistema, com menus e interação via console.
- **DatabaseManager.java**: Responsável pela conexão e criação inicial das tabelas no SQLite.
- **Model**: Entidades (Cliente, Eletronico, Funcionario, Pedido, etc.).
- **Service**: Contém a lógica de negócio e acesso ao banco de dados (CRUD, validações, etc.).
- **application.properties**: Configuração simples para URL do banco de dados (opcional se usar a string de conexão direto no código).

---

## 3. Dependências e Instalação

### 3.1. Pré-requisitos

1. **Java 21**
    - Verifique se sua máquina possui o Java 21 instalado. Você pode checar com:
      ```bash
      java -version
      ```
    - Caso não possua, faça o download do [JDK 21](https://www.oracle.com/java/technologies/downloads/) e instale.

2. **Maven**
    - Verifique se sua máquina possui o Maven instalado:
      ```bash
      mvn -version
      ```
    - Caso não possua, instale-o seguindo as instruções em [maven.apache.org](https://maven.apache.org/).

3. **SQLite**
    - Este projeto usa **SQLite** como banco de dados embutido (embedded). Não é necessário instalar o SQLite separadamente, pois o `sqlite-jdbc` já fornece o driver e o suporte para criar o arquivo `.db`.

### 3.2. Clonando o Projeto

Se você estiver usando **Git**, pode clonar este repositório com:

```bash
git clone https://github.com/hrvtonn/TechSale.git
```

Ou baixe o ZIP diretamente e extraia para uma pasta de sua preferência.

### 3.3. Compilando e Executando

1. Abra um terminal/prompt de comando na pasta do projeto (onde fica o arquivo `pom.xml`).
2. Execute o comando:
   ```bash
   mvn clean compile
   ```
   Isso vai baixar as dependências listadas no `pom.xml`, compilar o código e gerar os arquivos de classe em `target/classes`.

3. Para **executar** a aplicação via **console/terminal**, use:
   ```bash
   mvn exec:java '-Dexec.mainClass=com.techsale.Main'
   ```
   **Atenção**:
    - No **PowerShell**, use aspas simples ao redor de `-Dexec.mainClass`, pois o PowerShell pode interpretar incorretamente parâmetros com `=` se você usar aspas duplas.
    - Em outros terminais (Linux, Git Bash, etc.), pode funcionar também com `-Dexec.mainClass=com.techsale.Main` (sem aspas) ou com aspas duplas, a depender do ambiente.

4. **(Opcional)** Caso queira rodar no **IntelliJ IDEA**:
    - Abra o IntelliJ, vá em **File > Open** e selecione a pasta do projeto.
    - Aguarde o IntelliJ importar o Maven Project.
    - Vá em **Run > Edit Configurations**, crie uma nova configuração de execução, definindo a classe principal (`com.techsale.Main`), e rode.

---

## 4. Uso do Sistema

Ao executar, o **menu principal** será exibido:

```
=== Techsale: PDV de Eletronicos ===
1 - CRUD de Cliente
2 - CRUD de Eletronicos
3 - Realizar Pedido (Compra)
4 - Cancelar Pedido
5 - Emitir Fatura de Pedido
6 - CRUD de Funcionarios
7 - Calcular Salario do Funcionario
8 - Relatorio de Vendas por Cliente
9 - Sair
Selecione uma opcao:
```

### 4.1. CRUD de Cliente
- **Cadastrar Cliente**: Insira nome e endereço.
- **Atualizar Cliente**: Informe o ID, depois os novos dados.
- **Excluir Cliente**: Informe o ID do cliente a excluir.
- **Consultar Cliente**: Informe o ID para ver detalhes.

### 4.2. CRUD de Eletrônicos
- **Cadastrar Eletronico**: Nome, marca, preço e estoque.
- **Atualizar Eletronico**: Permite alterar dados já cadastrados.
- **Excluir Eletronico**: Remove o registro do banco.
- **Consultar Eletronico**: Informe o ID para visualizar.
- **Listar Eletronicos**: Exibe todos cadastrados.

### 4.3. Realizar Pedido
- Selecione um cliente **já cadastrado** ou crie um novo (ID = 0).
- Defina quanto o cliente pode gastar (limite).
- Adicione eletrônicos (com quantidade) ao pedido, respeitando:
    - **Estoque** disponível.
    - **Limite** de gasto.
- Escolha forma de pagamento (dinheiro ou cartão).
- O pedido será criado e o estoque dos itens será decrementado.

### 4.4. Cancelar Pedido
- Informe o **ID do pedido**.
- Caso o pedido já tenha uma fatura emitida, o cancelamento será bloqueado.

### 4.5. Emitir Fatura de Pedido
- Selecione o **ID do pedido**.
- Se o pedido estiver **cancelado**, a fatura não pode mais ser emitida.

### 4.6. CRUD de Funcionários
- **Cadastrar**: Define nome, tipo (1=Assalariado, 2=Comissionado, 3=Por Hora, 4=Base+Comissao), salário base, porcentagem de comissão e horas trabalhadas (conforme o tipo).
- **Atualizar**, **Excluir**, **Consultar**, **Listar**: Funciona de forma análoga ao CRUD de clientes e eletrônicos.

### 4.7. Calcular Salário do Funcionário
- Informe o **ID** do funcionário.
- O sistema fará o cálculo com base em:
    - **Tipo** do funcionário (assalariado, comissionado, etc.).
    - **salarioBase**, **porcentagemComissao**, **horasTrabalhadas**, etc.

### 4.8. Relatório de Vendas por Cliente
- Informe o **ID** do cliente.
- Opcionalmente, filtre por intervalo de datas no formato `yyyy-MM-dd HH:mm:ss`.
- O sistema listará:
    - Cada pedido, itens comprados, status (ativo/cancelado), se a fatura foi emitida e o valor total.
    - Ao final, o total gasto pelo cliente.

---

## 5. Observações Importantes

1. Se em algum momento você precisar **resetar** o banco de dados, basta:
    - **Apagar** o arquivo `techsale.db` (que fica na raiz do projeto).
    - Rodar novamente a aplicação. As tabelas serão criadas automaticamente (mas todos os dados serão perdidos).

2. O projeto é **exemplo básico** de CRUD + regras de negócio em Java, usando SQLite como BD local.
3. **Logs** simples são exibidos no console (ex.: `[LOG] Pedido criado com sucesso...`). Em um ambiente de produção, poderia ser substituído por um gerenciador de logs (Log4j, SLF4J etc.) e gravado em arquivos.

---

## 6. Contribuindo

Se quiser contribuir com melhorias:

1. Faça um **fork** do repositório.
2. Crie uma **branch** (ex.: `feature/nova-funcionalidade`).
3. Faça suas alterações e adicione testes se possível.
4. Abra um **Pull Request** descrevendo as mudanças.

---

## 7. Licença

Este projeto pode ser usado livremente para fins de estudo e demonstrações. Se tiver interesse em usá-lo comercialmente ou em projetos maiores, sinta-se à vontade para adaptar e estender as funcionalidades.

