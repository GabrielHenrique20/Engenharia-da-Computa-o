package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import dao.AtividadeDAO;
import model.Atividade;

// Classe de menu para operações relacionadas a Atividade

public class MenuAtividade {
    private AtividadeDAO atividadeDAO;
    private Scanner console = new Scanner(System.in);

    public MenuAtividade() throws Exception {
        atividadeDAO = new AtividadeDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Atividade");
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
                    buscarAtividade();
                    break;
                case 2:
                    incluirAtividade();
                    break;
                case 3:
                    alterarAtividade();
                    break;
                case 4:
                    excluirAtividade();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void buscarAtividade() {
        System.out.print("\nID da atividade: ");
        int id = console.nextInt();
        console.nextLine();
        try {
            Atividade atividade = atividadeDAO.buscarAtividade(id);
            if (atividade != null) {
                System.out.println(atividade);
            } else {
                System.out.println("Atividade não encontrada.");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void incluirAtividade() {
        System.out.println("\nInclusão de atividade");

        System.out.print("\nTitulo: ");
        String titulo = console.nextLine();
        System.out.print("Descricao: ");
        String descricao = console.nextLine();
        System.out.print("Data (DD/MM/AAAA): ");
        String data = console.nextLine();
        LocalDate dataAtividade = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Custo: ");
        float custo = console.nextFloat();
        console.nextLine();

        try {

            Atividade atividade = new Atividade();
            console.nextLine();
            atividade.setTitulo(titulo);
            atividade.setDescricao(descricao);
            atividade.setCusto(custo);
            atividade.setData(dataAtividade);

            if (atividadeDAO.incluirAtividade(atividade)) {System.out.println("Atividade incluído com sucesso.");System.out.println("ID gerado para atividade: " + atividade.getId());
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir atividade: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void alterarAtividade() {
        System.out.print("\nID da atividade a ser alterada: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Atividade atividade = atividadeDAO.buscarAtividade(id);
            if (atividade == null) {
                System.out.println("Atividade não encontrada.");
                return;
            }

            System.out.print("\nNovo titulo (vazio para manter): ");
            String titulo = console.nextLine();
            if (!titulo.isEmpty())
                atividade.setTitulo(titulo);

            System.out.print("Nova descrição (vazio para manter): ");
            String descricao = console.nextLine();
            if (!descricao.isEmpty())
                atividade.setDescricao(descricao);

            System.out.print("Nova data (DD/MM/AAAA, vazio para manter): ");
            String data = console.nextLine();
            if (!data.isEmpty())
                atividade.setData(LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            if (atividadeDAO.alterarAtividade(atividade)) {
                System.out.println("Atividade alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar atividade.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar atividade.");
        }
    }

    private void excluirAtividade() {
        System.out.print("\nID da atividade a ser excluída: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Atividade atividade = atividadeDAO.buscarAtividade(id);
            if (atividade == null) {
                System.out.println("Atividade não encontrada.");
                return;
            }

            System.out.print("Confirma exclusão? (S/N): ");
            char resp = console.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (atividadeDAO.excluirAtividade(id)) {
                    System.out.println("Atividade excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir atividade.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir atividade.");
        }
    }

    /*private void listarAtividadesPorCategoria() {
        System.out.print("\nID da categoria: ");
        int categoriaId = console.nextInt();
        console.nextLine();

        try {
            java.util.List<Atividade> atividades = atividadeDAO.listarPorCategoria(categoriaId);
            if (atividades.isEmpty()) {
                System.out.println("Nenhuma atividade encontrada para esta categoria.");
            } else {
                for (Atividade a : atividades) {
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar atividades por categoria: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}