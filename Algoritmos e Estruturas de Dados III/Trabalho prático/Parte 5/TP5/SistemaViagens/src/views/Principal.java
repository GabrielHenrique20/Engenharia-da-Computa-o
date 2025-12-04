package views;
import java.util.Scanner;
import controller.MenuAtividade;
import controller.MenuCategoria;
import controller.MenuUsuario;
import controller.MenuViagem;
import controller.MenuRelacaoViagemAtividade;
import controller.MenuBackup;

// Classe Principal do sistema de viagens 

public class Principal {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        int opcao;

        try {
            do {
                System.out.println("\n\nAEDsIII");
                System.out.println("-------");
                System.out.println("> Início");
                System.out.println("1 - ESCOLHA: Viagem"); // Casamento de padroes
                System.out.println("2 - ESCOLHA: Atividade");
                System.out.println("3 - ESCOLHA: Usuário"); 
                System.out.println("4 - ESCOLHA: Categoria");
                System.out.println("5 - ESCOLHA: Relação Viagem-Atividade");
                System.out.println("6 - ESCOLHA: Backup / Compactação");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        MenuViagem menuViagem = new MenuViagem();
                        menuViagem.menu();
                        break;
                    case 2:
                        MenuAtividade menuAtividade = new MenuAtividade();
                        menuAtividade.menu();
                        break;
                    case 3:
                        MenuUsuario menuUsuario = new MenuUsuario();
                        menuUsuario.menu();
                        break;
                    case 4:
                        MenuCategoria menuCategoria = new MenuCategoria();
                        menuCategoria.menu();
                        break;
                    case 5:
                        MenuRelacaoViagemAtividade menuRelacao = new MenuRelacaoViagemAtividade();
                        menuRelacao.menu();
                        break;
                    case 6:
                        MenuBackup menuBackup = new MenuBackup();
                        menuBackup.menu();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro fatal no sistema:");
            e.printStackTrace();
        } finally {
            console.close();
        }
    }
}