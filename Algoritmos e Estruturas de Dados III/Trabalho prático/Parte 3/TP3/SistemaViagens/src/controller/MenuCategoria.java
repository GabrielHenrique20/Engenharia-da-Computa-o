package controller;
import java.util.Scanner;
import dao.CategoriaDAO;
import model.Categoria;

// Classe de menu para operações relacionadas a Categoria

public class MenuCategoria {
    private CategoriaDAO categoriaDAO;
    private Scanner console = new Scanner(System.in);

    public MenuCategoria() throws Exception {
        categoriaDAO = new CategoriaDAO();
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
                System.out.println("Categoria incluída com sucesso.");
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
}