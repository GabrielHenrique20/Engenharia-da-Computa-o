// dao/ViagemDAO.java
package dao;


import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import model.Viagem;

public class ViagemDAO {
    private Arquivo<Viagem> arqViagem;
    
    // Índice 1:N: usuário -> viagens
    private ArvoreBMais<ParIntInt> ixUsuarioViagem;

    public ViagemDAO() throws Exception {
        arqViagem = new Arquivo<>("Viagem", Viagem.class.getConstructor());
        ixUsuarioViagem = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "relUsuarioViagem.db");
    }

    public Viagem buscarViagem(int id) throws Exception {
        return arqViagem.read(id);
    }

    public boolean incluirViagem(Viagem viagem) throws Exception {
        int id = arqViagem.create(viagem);
        if (id > 0) {
            // garante o 1:N
            ixUsuarioViagem.create(new ParIntInt(viagem.getIdUsuario(), id));
            return true;
        }
        return false;
    }

    public boolean alterarViagem(Viagem viagem) throws Exception {
        // 1) Ler o estado antigo para ver se o idUsuario mudou
        Viagem antiga = arqViagem.read(viagem.getId());
        if (antiga == null)
            return false;

        boolean ok = arqViagem.update(viagem);
        if (ok) {
            if (antiga.getIdUsuario() != viagem.getIdUsuario()) {
                // remove par antigo e insere o novo
                ixUsuarioViagem.delete(new ParIntInt(antiga.getIdUsuario(), viagem.getId()));
                ixUsuarioViagem.create(new ParIntInt(viagem.getIdUsuario(), viagem.getId()));
            }
        }
        return ok;
    }

    public boolean excluirViagem(int id) throws Exception {
        // 1) Antes de excluir, remover do índice
        Viagem v = arqViagem.read(id);
        if (v != null) {
            ixUsuarioViagem.delete(new ParIntInt(v.getIdUsuario(), v.getId()));
        }
        // Remover relações N:N (Viagem-Atividade)
        try {
            RelViagemAtividadeDAO rel = new RelViagemAtividadeDAO();
            rel.removerTodosDaViagem(id);
        } catch (Exception ignore) {}
        return arqViagem.delete(id);
    }


    // listar todas as viagens (varrendo todos os arquivos de dados para o casamento de padroes)
    public java.util.List<Viagem> listarTodas() throws Exception {
        java.util.ArrayList<Viagem> out = new java.util.ArrayList<>();
        for (Viagem v : arqViagem.readAll()) {
            if (v != null) {
                out.add(v);
            }
        }
        return out;
    }

    // listar viagens por usuário, reaproveitando o índice
    public java.util.List<Viagem> listarPorUsuario(int userId) throws Exception {
        java.util.ArrayList<Viagem> out = new java.util.ArrayList<>();
        for (ParIntInt p : ixUsuarioViagem.read(new ParIntInt(userId, -1))) {
            Viagem v = arqViagem.read(p.get2());
            if (v != null)
                out.add(v);
        }
        return out;
    }
}
