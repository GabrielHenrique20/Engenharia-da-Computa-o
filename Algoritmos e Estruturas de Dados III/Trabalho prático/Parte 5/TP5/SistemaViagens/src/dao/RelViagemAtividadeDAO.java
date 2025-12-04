package dao;

import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import model.Atividade;
import model.Viagem;
import model.RelViagemAtividade;

/**
 * Relação N:N Viagem <-> Atividade com atributos do relacionamento:
 * - status (String)
 * - prioridade (int)
 *
 * Armazenamento:
 * - Arquivo<RelViagemAtividade> arqRel (PK própria do vínculo)
 * - Índices B+: viagemId -> relId (ixViagemRel) e atividadeId -> relId
 * (ixAtividadeRel)
 */
public class RelViagemAtividadeDAO {

    // Atributos
    private Arquivo<RelViagemAtividade> arqRel;
    private ArvoreBMais<ParIntInt> ixViagemRel;
    private ArvoreBMais<ParIntInt> ixAtividadeRel;

    private ViagemDAO viagemDAO;
    private AtividadeDAO atividadeDAO;

    // Construtor
    public RelViagemAtividadeDAO() throws Exception {
        arqRel = new Arquivo<>("RelViagemAtividade", RelViagemAtividade.class.getConstructor());
        ixViagemRel = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "relViagemAtividade.db");
        ixAtividadeRel = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "relAtividadeViagem.db");
        viagemDAO = new ViagemDAO();
        atividadeDAO = new AtividadeDAO();
    }

    // Métodos CRUD e listagens específicas
    public boolean vincular(int idViagem, int idAtividade, String status, int prioridade) throws Exception {
        if (viagemDAO.buscarViagem(idViagem) == null)
            return false;
        if (atividadeDAO.buscarAtividade(idAtividade) == null)
            return false;
        // Evita duplicidade
        for (ParIntInt p : ixViagemRel.read(new ParIntInt(idViagem, -1))) {
            RelViagemAtividade r = arqRel.read(p.get2());
            if (r != null && r.getIdAtividade() == idAtividade)
                return true;
        }
        RelViagemAtividade novo = new RelViagemAtividade(-1, idViagem, idAtividade, status, prioridade);
        int relId = arqRel.create(novo);
        if (relId > 0) {
            ixViagemRel.create(new ParIntInt(idViagem, relId));
            ixAtividadeRel.create(new ParIntInt(idAtividade, relId));
            return true;
        }
        return false;
    }

    public boolean alterarAtributos(int idViagem, int idAtividade, String status, int prioridade) throws Exception {
        Integer relId = localizarRelId(idViagem, idAtividade);
        if (relId == null)
            return false;
        RelViagemAtividade r = arqRel.read(relId);
        if (r == null)
            return false;
        r.setStatus(status);
        r.setPrioridade(prioridade);
        return arqRel.update(r);
    }

    public boolean desvincular(int idViagem, int idAtividade) throws Exception {
        Integer relId = localizarRelId(idViagem, idAtividade);
        if (relId == null)
            return false;
        RelViagemAtividade r = arqRel.read(relId);
        if (r == null)
            return false;
        ixViagemRel.delete(new ParIntInt(idViagem, relId));
        ixAtividadeRel.delete(new ParIntInt(idAtividade, relId));
        return arqRel.delete(relId);
    }

    private Integer localizarRelId(int idViagem, int idAtividade) throws Exception {
        for (ParIntInt p : ixViagemRel.read(new ParIntInt(idViagem, -1))) {
            RelViagemAtividade r = arqRel.read(p.get2());
            if (r != null && r.getIdAtividade() == idAtividade)
                return p.get2();
        }
        return null;
    }

    public java.util.List<RelViagemAtividade> listarRelacoesDaViagem(int idViagem) throws Exception {
        java.util.ArrayList<RelViagemAtividade> out = new java.util.ArrayList<>();
        for (ParIntInt p : ixViagemRel.read(new ParIntInt(idViagem, -1))) {
            RelViagemAtividade r = arqRel.read(p.get2());
            if (r != null)
                out.add(r);
        }
        return out;
    }

    public java.util.List<RelViagemAtividade> listarRelacoesDaAtividade(int idAtividade) throws Exception {
        java.util.ArrayList<RelViagemAtividade> out = new java.util.ArrayList<>();
        for (ParIntInt p : ixAtividadeRel.read(new ParIntInt(idAtividade, -1))) {
            RelViagemAtividade r = arqRel.read(p.get2());
            if (r != null)
                out.add(r);
        }
        return out;
    }

    public java.util.List<Atividade> listarAtividadesDaViagem(int idViagem) throws Exception {
        java.util.ArrayList<Atividade> out = new java.util.ArrayList<>();
        for (RelViagemAtividade r : listarRelacoesDaViagem(idViagem)) {
            Atividade a = atividadeDAO.buscarAtividade(r.getIdAtividade());
            if (a != null)
                out.add(a);
        }
        return out;
    }

    public java.util.List<Viagem> listarViagensDaAtividade(int idAtividade) throws Exception {
        java.util.ArrayList<Viagem> out = new java.util.ArrayList<>();
        for (RelViagemAtividade r : listarRelacoesDaAtividade(idAtividade)) {
            Viagem v = viagemDAO.buscarViagem(r.getIdViagem());
            if (v != null)
                out.add(v);
        }
        return out;
    }

    // Cascatas
    public void removerTodosDaViagem(int idViagem) throws Exception {
        java.util.ArrayList<Integer> relIds = new java.util.ArrayList<>();
        for (ParIntInt p : ixViagemRel.read(new ParIntInt(idViagem, -1))) {
            relIds.add(p.get2());
        }
        for (Integer rid : relIds) {
            RelViagemAtividade r = arqRel.read(rid);
            if (r != null) {
                ixAtividadeRel.delete(new ParIntInt(r.getIdAtividade(), rid));
                ixViagemRel.delete(new ParIntInt(idViagem, rid));
                arqRel.delete(rid);
            }
        }
    }

    public void removerTodosDaAtividade(int idAtividade) throws Exception {
        java.util.ArrayList<Integer> relIds = new java.util.ArrayList<>();
        for (ParIntInt p : ixAtividadeRel.read(new ParIntInt(idAtividade, -1))) {
            relIds.add(p.get2());
        }
        for (Integer rid : relIds) {
            RelViagemAtividade r = arqRel.read(rid);
            if (r != null) {
                ixViagemRel.delete(new ParIntInt(r.getIdViagem(), rid));
                ixAtividadeRel.delete(new ParIntInt(idAtividade, rid));
                arqRel.delete(rid);
            }
        }
    }
}
