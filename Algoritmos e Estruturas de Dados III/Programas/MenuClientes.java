import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuClientes {
    private ClienteDAO clienteDAO;
    private Scanner console = new Scanner(System.in);

    public MenuClientes() throws Exception {
        clienteDAO = new ClienteDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Clientes");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarCliente();
                    break;
                case 2:
                    incluirCliente();
                    break;
                case 3:
                    alterarCliente();
                    break;
                case 4:
                    excluirCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void buscarCliente() {
        System.out.print("\nID do cliente: ");
        int id = console.nextInt();
        console.nextLine();
        try {
            Cliente cliente = clienteDAO.buscarCliente(id);
            if (cliente != null) {
                System.out.println(cliente);
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente.");
        }
    }

    private void incluirCliente() {
        System.out.println("\nInclusão de cliente");

        System.out.print("\nNome: ");
        String nome = console.nextLine();
        System.out.print("CPF (11 dígitos): ");
        String cpf = console.nextLine();
        System.out.print("Salário: ");
        float salario = console.nextFloat();
        console.nextLine();
        System.out.print("Data de nascimento (DD/MM/AAAA): ");
        String dataStr = console.nextLine();
        LocalDate nascimento = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            Cliente cliente = new Cliente(nome, cpf, salario, nascimento);
            if (clienteDAO.incluirCliente(cliente)) {
                System.out.println("Cliente incluído com sucesso.");
            } else {
                System.out.println("Erro ao incluir cliente.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir cliente.");
        }
    }

    private void alterarCliente() {
        System.out.print("\nID do cliente a ser alterado: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Cliente cliente = clienteDAO.buscarCliente(id);
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.print("\nNovo nome (vazio para manter): ");
            String nome = console.nextLine();
            if (!nome.isEmpty()) cliente.setNome(nome);

            System.out.print("Novo CPF (vazio para manter): ");
            String cpf = console.nextLine();
            if (!cpf.isEmpty()) cliente.setCpf(cpf);

            System.out.print("Novo salário (vazio para manter): ");
            String salarioStr = console.nextLine();
            if (!salarioStr.isEmpty()) cliente.setSalario(Float.parseFloat(salarioStr));

            System.out.print("Nova data de nascimento (DD/MM/AAAA, vazio para manter): ");
            String dataStr = console.nextLine();
            if (!dataStr.isEmpty()) cliente.setNascimento(LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            if (clienteDAO.alterarCliente(cliente)) {
                System.out.println("Cliente alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar cliente.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar cliente.");
        }
    }

    private void excluirCliente() {
        System.out.print("\nID do cliente a ser excluído: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Cliente cliente = clienteDAO.buscarCliente(id);
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.print("Confirma exclusão? (S/N): ");
            char resp = console.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (clienteDAO.excluirCliente(id)) {
                    System.out.println("Cliente excluído com sucesso.");
                } else {
                    System.out.println("Erro ao excluir cliente.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir cliente.");
        }
    }
}
