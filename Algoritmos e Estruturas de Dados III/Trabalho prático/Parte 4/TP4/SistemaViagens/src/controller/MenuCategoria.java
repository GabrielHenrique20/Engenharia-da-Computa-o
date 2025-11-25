package controller;

import java.util.Scanner;
import dao.CategoriaDAO;
import dao.AtividadeDAO;
import model.Categoria;
import model.Atividade;

// SOBRE A RELAÇÃO CATEGORIA -> ATIVIDADE (1:N):
// - A classe Atividade possui um atributo idCategoria (FK) que referencia a Categoria a qual pertence.
// - A classe AtividadeDAO implementa a manutenção do índice 1:N (inclusão, alteração e exclusão de atividades já atualizam o índice).
// - A classe MenuAtividade implementa a listagem de atividades por categoria, usando o índice.
// - A classe MenuCategoria não precisa ser alterada, pois a manutenção do índice já é feita automaticamente pelo AtividadeDAO.

public class MenuCategoria {
    private CategoriaDAO categoriaDAO;
    private AtividadeDAO atividadeDAO;
    private Scanner console = new Scanner(System.in);

    public MenuCategoria() throws Exception {
        categoriaDAO = new CategoriaDAO();
        atividadeDAO = new AtividadeDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Categoria");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Associar Atividade à Categoria");
            System.out.println("6 - Listar Atividades da Categoria");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarCategoria();
                    break;
                case 2:
                    incluirCategoria();
                    break;
                case 3:
                    alterarCategoria();
                    break;
                case 4:
                    excluirCategoria();
                    break;
                case 5:
                    associarAtividadeCategoria();
                    break;
                case 6:
                    listarAtividadesCategoria();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void buscarCategoria() {
        System.out.print("\nID da categoria: ");
        int id = console.nextInt();
        console.nextLine();
        try {
            Categoria categoria = categoriaDAO.buscarCategoria(id);
            if (categoria != null) {
                System.out.println(categoria);
            } else {
                System.out.println("Categoria não encontrada.");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void incluirCategoria() {
        System.out.println("\nInclusão de categoria");

        System.out.print("\nNome: ");
        String nome = console.nextLine();

        try {

            Categoria categoria = new Categoria();
            categoria.setNome(nome);

            if (categoriaDAO.incluirCategoria(categoria)) {
                System.out.println("Categoria incluído com sucesso.");
                System.out.println("ID gerado para categoria: " + categoria.getId());
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir categoria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void alterarCategoria() {
        System.out.print("\nID da categoria a ser alterada: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Categoria categoria = categoriaDAO.buscarCategoria(id);
            if (categoria == null) {
                System.out.println("Categoria não encontrada.");
                return;
            }

            System.out.print("\nNovo nome (vazio para manter): ");
            String nome = console.nextLine();
            if (!nome.isEmpty())
                categoria.setNome(nome);

            if (categoriaDAO.alterarCategoria(categoria)) {
                System.out.println("Categoria alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar categoria.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar categoria.");
        }
    }

    private void excluirCategoria() {
        System.out.print("\nID da categoria a ser excluída: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Categoria categoria = categoriaDAO.buscarCategoria(id);
            if (categoria == null) {
                System.out.println("Categoria não encontrada.");
                return;
            }

            System.out.print("Confirma exclusão? (S/N): ");
            char resp = console.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (categoriaDAO.excluirCategoria(id)) {
                    System.out.println("Categoria excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir categoria.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir categoria.");
        }
    }

    // --- Métodos para gerenciar relação 1:N Categoria -> Atividade ---

    private void associarAtividadeCategoria() {
        System.out.print("ID da Categoria: ");
        int categoriaId = Integer.parseInt(console.nextLine());

        try {
            Categoria cat = categoriaDAO.buscarCategoria(categoriaId);
            if (cat == null) {
                System.out.println("Categoria não encontrada.");
                return;
            }

            System.out.print("ID da Atividade a associar: ");
            int atividadeId = Integer.parseInt(console.nextLine());

            Atividade atividade = atividadeDAO.buscarAtividade(atividadeId);
            if (atividade == null) {
                System.out.println("Atividade não encontrada.");
                return;
            }

            atividade.setIdCategoria(categoriaId);
            if (atividadeDAO.alterarAtividade(atividade)) {
                System.out.println("Atividade associada à categoria com sucesso.");
            } else {
                System.out.println("Não foi possível associar a atividade à categoria.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao associar atividade à categoria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarAtividadesCategoria() {
        System.out.print("ID da Categoria: ");
        int categoriaId = Integer.parseInt(console.nextLine());

        try {
            Categoria cat = categoriaDAO.buscarCategoria(categoriaId);
            if (cat == null) {
                System.out.println("Categoria não encontrada.");
                return;
            }

            java.util.List<Atividade> atividades = atividadeDAO.listarPorCategoria(categoriaId);
            if (atividades.isEmpty()) {
                System.out.println("Nenhuma atividade associada a esta categoria.");
            } else {
                System.out.println("Atividades da categoria " + categoriaId + " - " + cat.getNome() + ":");
                for (Atividade a : atividades) {
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar atividades da categoria: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
