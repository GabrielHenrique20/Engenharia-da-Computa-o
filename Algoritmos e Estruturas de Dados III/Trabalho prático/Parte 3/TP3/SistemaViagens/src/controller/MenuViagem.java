package controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import dao.ViagemDAO;
import model.Viagem;

// Classe de menu para operações relacionadas a Viagem

public class MenuViagem {
    private ViagemDAO viagemDAO;
    private Scanner console = new Scanner(System.in);

    public MenuViagem() throws Exception {
        viagemDAO = new ViagemDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Viagem");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarViagem();
                    break;
                case 2:
                    incluirViagem();
                    break;
                case 3:
                    alterarViagem();
                    break;
                case 4:
                    excluirViagem();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void buscarViagem() {
        System.out.print("\nID da viagem: ");
        int id = console.nextInt();
        console.nextLine();
        try {
            Viagem viagem = viagemDAO.buscarViagem(id);
            if (viagem != null) {
                System.out.println(viagem);
            } else {
                System.out.println("Viagem não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar viagem.");
        }
    }

    private void incluirViagem() {
        System.out.println("\nInclusão de viagem");

        System.out.print("\nDestino: ");
        String destino = console.nextLine();
        System.out.print("Orçamento: ");
        float orcamento = console.nextFloat();
        console.nextLine();
        System.out.print("Data de Inicio (DD/MM/AAAA): ");
        String dataInicio = console.nextLine();
        LocalDate data_inicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Data de Fim (DD/MM/AAAA): ");
        String dataFim = console.nextLine();
        LocalDate data_fim = LocalDate.parse(dataFim, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("ID do Usuário: ");
        int idUsuario = console.nextInt();
        console.nextLine();

        try {

            Viagem viagem = new Viagem();
            viagem.setIdUsuario(idUsuario);
            viagem.setDestino(destino);
            viagem.setOrcamento(orcamento);
            viagem.setDataInicio(data_inicio);
            viagem.setDataFim(data_fim);

            if (viagemDAO.incluirViagem(viagem)) {System.out.println("Viagem incluído com sucesso.");System.out.println("ID gerado para viagem: " + viagem.getId());
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir viagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void alterarViagem() {
        System.out.print("\nID da viagem a ser alterada: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Viagem viagem = viagemDAO.buscarViagem(id);
            if (viagem == null) {
                System.out.println("Viagem não encontrada.");
                return;
            }

            System.out.print("\nNovo destino (vazio para manter): ");
            String destino = console.nextLine();
            if (!destino.isEmpty())
                viagem.setDestino(destino);

            System.out.print("Novo orçamento (0 para manter): ");
            float orcamento = console.nextFloat();
            console.nextLine();

            if (orcamento != 0)
                viagem.setOrcamento(orcamento);

            System.out.print("Nova data de início (DD/MM/AAAA, vazio para manter): ");
            String dataInicio = console.nextLine();
            if (!dataInicio.isEmpty())
                viagem.setDataInicio(LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            System.out.print("Nova data de fim (DD/MM/AAAA, vazio para manter): ");
            String dataFim = console.nextLine();
            if (!dataFim.isEmpty())
                viagem.setDataFim(LocalDate.parse(dataFim, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            if (viagemDAO.alterarViagem(viagem)) {
                System.out.println("Viagem alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar viagem.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar viagem.");
        }
    }

    private void excluirViagem() {
        System.out.print("\nID da viagem a ser excluída: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Viagem viagem = viagemDAO.buscarViagem(id);
            if (viagem == null) {
                System.out.println("Viagem não encontrada.");
                return;
            }

            System.out.print("Confirma exclusão? (S/N): ");
            char resp = console.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (viagemDAO.excluirViagem(id)) {
                    System.out.println("Viagem excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir viagem.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir viagem.");
        }
    }
}
