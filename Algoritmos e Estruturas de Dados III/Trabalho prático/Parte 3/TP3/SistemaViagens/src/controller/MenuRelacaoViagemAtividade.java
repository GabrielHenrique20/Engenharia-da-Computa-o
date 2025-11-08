package controller;

import java.util.Scanner;
import dao.RelViagemAtividadeDAO;
import model.RelViagemAtividade;

// Classe de menu para operações relacionadas à relação Viagem-Atividade
// N:N com atributos próprios (status, prioridade)

@SuppressWarnings("unused")
public class MenuRelacaoViagemAtividade {
    private Scanner console = new Scanner(System.in);
    private RelViagemAtividadeDAO relDAO;

    public MenuRelacaoViagemAtividade() throws Exception {
        relDAO = new RelViagemAtividadeDAO();
    }

    public void menu() {
        int opcao = -1;
        do {
            System.out.println("\nRelação Viagem <-> Atividade (N:N)");
            System.out.println("1 - Vincular atividade a uma viagem (com status/prioridade)");
            System.out.println("2 - Alterar status/prioridade de um vínculo");
            System.out.println("3 - Desvincular atividade de uma viagem");
            System.out.println("4 - Listar vínculos de uma viagem");
            System.out.println("5 - Listar vínculos de uma atividade");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = console.nextInt();
            console.nextLine();

            try {
                switch (opcao) {
                    case 1: {
                        System.out.print("ID da Viagem: ");
                        int idV = console.nextInt();
                        console.nextLine();
                        System.out.print("ID da Atividade: ");
                        int idA = console.nextInt();
                        console.nextLine();
                        System.out.print("Status do planejamento: ");
                        String status = console.nextLine();
                        System.out.print("Prioridade (número): ");
                        int prioridade = console.nextInt();
                        console.nextLine();
                        if (relDAO.vincular(idV, idA, status, prioridade))
                            System.out.println("Vínculo criado/confirmado.");
                        else
                            System.out.println("Falha ao vincular (ids inválidos?).");
                    }
                        break;
                    case 2: {
                        System.out.print("ID da Viagem: ");
                        int idV = console.nextInt();
                        console.nextLine();
                        System.out.print("ID da Atividade: ");
                        int idA = console.nextInt();
                        console.nextLine();
                        System.out.print("Novo status: ");
                        String status = console.nextLine();
                        System.out.print("Nova prioridade (número): ");
                        int prioridade = console.nextInt();
                        console.nextLine();
                        if (relDAO.alterarAtributos(idV, idA, status, prioridade))
                            System.out.println("Atributos atualizados.");
                        else
                            System.out.println("Vínculo não encontrado.");
                    }
                        break;
                    case 3: {
                        System.out.print("ID da Viagem: ");
                        int idV = console.nextInt();
                        console.nextLine();
                        System.out.print("ID da Atividade: ");
                        int idA = console.nextInt();
                        console.nextLine();
                        if (relDAO.desvincular(idV, idA))
                            System.out.println("Desvinculado com sucesso.");
                        else
                            System.out.println("Vínculo não encontrado.");
                    }
                        break;
                    /*
                     * case 4: {
                     * System.out.print("ID da Viagem: ");
                     * int idV = console.nextInt();
                     * console.nextLine();
                     * for (RelViagemAtividade r : relDAO.listarRelacoesDaViagem(idV)) {
                     * System.out.println(r);
                     * }
                     * }
                     * break;
                     */
                    case 4: {
                        System.out.print("ID da Viagem: ");
                        int idV = console.nextInt();
                        console.nextLine();

                        java.util.List<model.RelViagemAtividade> rels = relDAO.listarRelacoesDaViagem(idV);
                        if (rels.isEmpty()) {
                            System.out.println("Nenhuma atividade vinculada a esta viagem.");
                            break;
                        }

                        dao.ViagemDAO vdao = new dao.ViagemDAO();
                        dao.AtividadeDAO adao = new dao.AtividadeDAO();

                        model.Viagem v = vdao.buscarViagem(idV);
                        if (v == null) {
                            System.out.println("Viagem não encontrada.");
                            break;
                        }

                        System.out.println("\nViagem #" + v.getId() + " - " + v.getDestino());
                        System.out.println("Atividades vinculadas:");
                        System.out.println("---------------------------------------------------------------");
                        System.out.printf("%-4s  %-28s  %-10s  %-12s  %-12s%n",
                                "ID", "Título", "Data", "Status", "Planejamento");
                        System.out.println("---------------------------------------------------------------");

                        //  Ordenar por prioridade desc e por data asc para dar um senso de
                        // "planejamento"
                        rels.sort((a, b) -> {
                            int cmp = Integer.compare(b.getPrioridade(), a.getPrioridade());
                            if (cmp != 0)
                                return cmp;
                            try {
                                model.Atividade aa = adao.buscarAtividade(a.getIdAtividade());
                                model.Atividade bb = adao.buscarAtividade(b.getIdAtividade());
                                java.time.LocalDate da = (aa != null ? aa.getData() : java.time.LocalDate.MIN);
                                java.time.LocalDate db = (bb != null ? bb.getData() : java.time.LocalDate.MIN);
                                return da.compareTo(db);
                            } catch (Exception e) {
                                return 0;
                            }
                        });

                        for (model.RelViagemAtividade r : rels) {
                            model.Atividade a = adao.buscarAtividade(r.getIdAtividade());
                            if (a == null) {
                                System.out.printf("%-4s  %-28s  %-10s  %-12s  %-12s%n",
                                        r.getIdAtividade(), "(não encontrada)", "-", r.getStatus(),
                                        formatPlanejamento(r.getPrioridade()));
                                continue;
                            }
                            System.out.printf("%-4d  %-28s  %-10s  %-12s  %-12s%n",
                                    a.getId(),
                                    abreviar(a.getTitulo(), 28),
                                    String.valueOf(a.getData()),
                                    abreviar(r.getStatus(), 12),
                                    formatPlanejamento(r.getPrioridade()));
                        }
                        System.out.println("---------------------------------------------------------------");
                    }
                        break;

                    case 5: {
                        System.out.print("ID da Atividade: ");
                        int idA = console.nextInt();
                        console.nextLine();

                        // Pega os vínculos (para obter status/prioridade)
                        java.util.List<model.RelViagemAtividade> rels = relDAO.listarRelacoesDaAtividade(idA);
                        if (rels.isEmpty()) {
                            System.out.println("Nenhuma viagem vinculada a esta atividade.");
                        } else {
                            dao.ViagemDAO vdao = new dao.ViagemDAO();
                            System.out.println("\nViagens vinculadas à atividade " + idA + ":");
                            for (model.RelViagemAtividade r : rels) {
                                model.Viagem v = vdao.buscarViagem(r.getIdViagem());
                                if (v != null) {
                                    System.out.println(v); // imprime a Viagem com destino/orçamento/datas
                                    System.out.println("  Status.....: " + r.getStatus());
                                    System.out.println("  Prioridade.: " + r.getPrioridade());
                                    System.out.println("-------------------------------------");
                                } else {
                                    System.out.println("(Viagem " + r.getIdViagem() + " não encontrada)");
                                }
                            }
                        }
                    }
                        break;

                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
        } while (opcao != 0);
    }

    private static String abreviar(String s, int max) {
        if (s == null)
            return "-";
        if (s.length() <= max)
            return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

    // Mapeia a prioridade numérica para um rótulo de "planejamento".
    private static String formatPlanejamento(int prioridade) {
        if (prioridade >= 9)
            return "Altíssimo";
        if (prioridade >= 7)
            return "Alto";
        if (prioridade >= 4)
            return "Médio";
        if (prioridade > 0)
            return "Baixo";
        return "—";
    }

}
