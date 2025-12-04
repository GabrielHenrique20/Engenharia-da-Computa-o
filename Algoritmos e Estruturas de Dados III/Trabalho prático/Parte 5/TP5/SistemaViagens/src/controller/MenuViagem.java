package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import dao.ViagemDAO;
import model.Viagem;
import casamento.KMP;
import casamento.BoyerMoore;

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
            System.out.println("5 - Pesquisar por padrão (KMP / Boyer-Moore)");
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
                case 5:
                    pesquisarPorDestino();
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
        System.out.print("ID do usuário responsável pela viagem: ");
        int idUsuario = console.nextInt();
        console.nextLine();

        try {

            Viagem viagem = new Viagem();
            viagem.setIdUsuario(idUsuario);
            viagem.setDestino(destino);
            viagem.setOrcamento(orcamento);
            viagem.setDataInicio(data_inicio);
            viagem.setDataFim(data_fim);

            if (viagemDAO.incluirViagem(viagem)) {
                System.out.println("Viagem incluído com sucesso.");
                System.out.println("ID gerado para viagem: " + viagem.getId());
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

    // Pesquisa de viagens por padrão no destino, usando KMP ou Boyer-Moore

    private void pesquisarPorDestino() {
        System.out.println("\nPesquisa de viagens por DESTINO (KMP / Boyer-Moore)");
        System.out.print("Informe o padrão/termo a ser buscado no destino: ");
        String padrao = console.nextLine();

        if (padrao == null || padrao.isEmpty()) {
            System.out.println("Padrão vazio. Operação cancelada.");
            return;
        }

        System.out.println("\nEscolha o algoritmo de casamento de padrão:");
        System.out.println("1 - KMP");
        System.out.println("2 - BoyerMoore (bad character)");
        System.out.print("Opção: ");
        int alg;
        try {
            alg = Integer.parseInt(console.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }

        try {
            java.util.List<Viagem> todas = viagemDAO.listarTodas();
            java.util.List<Viagem> encontradas = new java.util.ArrayList<>();

            String padraoNorm = padrao.toLowerCase();

            for (Viagem v : todas) {
                String destino = v.getDestino();
                if (destino == null)
                    continue;

                String destinoNorm = destino.toLowerCase();
                boolean casou;

                if (alg == 1) {
                    casou = KMP.match(destinoNorm, padraoNorm);
                } else if (alg == 2) {
                    casou = BoyerMoore.match(destinoNorm, padraoNorm);
                } else {
                    System.out.println("Opção de algoritmo inválida.");
                    return;
                }

                if (casou) {
                    encontradas.add(v);
                }
            }

            if (encontradas.isEmpty()) {
                System.out.println("\nNenhuma viagem encontrada cujo destino contenha o padrão informado.");
            } else {
                System.out.println("\nViagens encontradas:");
                for (Viagem v : encontradas) {
                    System.out.println(v);
                    System.out.println("----------------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar pesquisa por padrão: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
