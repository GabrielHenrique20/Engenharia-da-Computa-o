package controller;
import java.util.ArrayList;
import java.util.Scanner;
import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import dao.UsuarioDAO;
import model.Usuario;
import dao.Arquivo;
import dao.ArquivoUsuario;
import dao.ViagemDAO;
import model.Viagem;

// SOBRE A RELAÇÃO 1:N (Usuario -> VIAGEM):
// - A classe ViagemDAO já implementa a manutenção do índice 1:N
//   (inclusão, alteração e exclusão de viagens já atualizam o índice).
// - A classe MenuUsuario implementa a associação manual de viagens a usuários
//   e a listagem de viagens de um usuário, usando o índice.
// - A classe MenuViagem não precisa ser alterada, pois a manutenção do índice
//   já é feita automaticamente pelo ViagemDAO.


@SuppressWarnings("unused")
public class MenuUsuario {
    private UsuarioDAO usuarioDAO;
    private Scanner console = new Scanner(System.in);

    // criacao de variaveis para manipular arquivos
    private ViagemDAO viagemDAO;

    // árvores B+
    private ArvoreBMais<ParIntInt> relviagemUsuarioArvoreBMais; // pegando o id de cada uma

    public MenuUsuario() {
        try {
            usuarioDAO = new UsuarioDAO();
            viagemDAO = new ViagemDAO();
            relviagemUsuarioArvoreBMais = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4,
                    "relviagemUsuarioArvoreBMais.db");
        } catch (Exception e) {
            System.out.println("Erro ao criar MenuUsuario: " + e.getMessage());
            e.printStackTrace(); // adiciona stack trace para debug
        }
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Usuário");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Associar Viagem a Usuário"); 
            System.out.println("6 - Listar Viagens do Usuário"); 
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarUsuario();
                    break;
                case 2:
                    incluirUsuario();
                    break;
                case 3:
                    alterarUsuario();
                    break;
                case 4:
                    excluirUsuario();
                    break;
                case 5:
                    associarViagemUsuario();
                    break;
                case 6:
                    listarViagensUsuario();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    private void buscarUsuario() {
        System.out.print("\nID do Usuário: ");
        int id = console.nextInt();
        console.nextLine();
        try {
            Usuario usuario = usuarioDAO.buscarUsuario(id);
            if (usuario != null) {
                System.out.println(usuario);
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário.");
        }
    }

    private void incluirUsuario() {
        System.out.println("\nInclusão de usuário");

        System.out.print("\nNome: ");
        String nome = console.nextLine();

        System.out.print("Digite os telefones separados por vírgula: ");
        String linha = console.nextLine();

        String[] telefones = linha.split(",");
        for (int i = 0; i < telefones.length; i++) {
            telefones[i] = telefones[i].trim();
        }

        System.out.print("Email: ");
        String email = console.nextLine();

        try {

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setTelefone(telefones);
            usuario.setEmail(email);

            if (usuarioDAO.incluirUsuario(usuario)) {
                System.out.println("Usuário incluído com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void alterarUsuario() {
        System.out.print("\nID do usuário a ser alterado: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Usuario usuario = usuarioDAO.buscarUsuario(id);
            if (usuario == null) {
                System.out.println("Usuário não encontrado.");
                return;
            }

            System.out.print("\nNovo nome (vazio para manter): ");
            String nome = console.nextLine();
            if (!nome.isEmpty())
                usuario.setNome(nome);

            System.out.print("\nNovos telefones (vazio para manter): ");
            String linha = console.nextLine();

            if (!linha.isEmpty()) {
                String[] telefone = linha.split(",");
                for (int i = 0; i < telefone.length; i++) {
                    telefone[i] = telefone[i].trim();
                }
                usuario.setTelefone(telefone);
            }

            System.out.print("Novo email (vazio para manter): ");
            String email = console.nextLine();
            if (!email.isEmpty()) {
                usuario.setEmail(email);
            }

            if (usuarioDAO.alterarUsuario(usuario)) {
                System.out.println("Usuário alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar usuário.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar usuário.");
        }
    }

    private void excluirUsuario() {
        System.out.print("\nID do usuário a ser excluído: ");
        int id = console.nextInt();
        console.nextLine();

        try {
            Usuario usuario = usuarioDAO.buscarUsuario(id);
            if (usuario == null) {
                System.out.println("Usuário não encontrada.");
                return;
            }

            System.out.print("Confirma exclusão? (S/N): ");
            char resp = console.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                if (usuarioDAO.excluirUsuario(id)) {
                    System.out.println("Usuário excluído com sucesso.");
                } else {
                    System.out.println("Erro ao excluir usuário.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário.");
        }
    }

    // --- Novos métodos para gerenciar relação 1:N ---

    private void associarViagemUsuario() {
        System.out.print("\nID do Usuário: ");
        int userId = console.nextInt();
        console.nextLine();
        try {
            Usuario u = usuarioDAO.buscarUsuario(userId);
            if (u == null) {
                System.out.println("Usuário não encontrado.");
                return;
            }

            System.out.print("ID da Viagem a associar: ");
            int viagemId = console.nextInt();
            console.nextLine();

            Viagem v = viagemDAO.buscarViagem(viagemId);
            if (v == null) {
                System.out.println("Viagem não encontrada.");
                return;
            }

            ParIntInt rel = new ParIntInt(userId, viagemId);
            boolean ok = relviagemUsuarioArvoreBMais.create(rel);
            if (ok) {
                System.out.println("Associação usuário->viagem criada com sucesso.");
            } else {
                System.out.println("Não foi possível criar a associação (talvez já exista).");
            }
        } catch (Exception e) {
            System.out.println("Erro ao associar viagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarViagensUsuario() {
        System.out.print("\nID do Usuário: ");
        int userId = console.nextInt();
        console.nextLine();

        try {
            Usuario u = usuarioDAO.buscarUsuario(userId);
            if (u == null) {
                System.out.println("Usuário não encontrado.");
                return;
            }

            ArrayList<ParIntInt> rels = relviagemUsuarioArvoreBMais.read(new ParIntInt(userId, -1));
            if (rels.isEmpty()) {
                System.out.println("Nenhuma viagem associada a este usuário.");
                return;
            }

            System.out.println("\nViagens do usuário " + userId + ":");
            for (ParIntInt p : rels) {
                Viagem v = viagemDAO.buscarViagem(p.get2());
                if (v != null)
                    System.out.println(v);
                else
                    System.out.println("Viagem id=" + p.get2() + " (não encontrada)");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar viagens do usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
