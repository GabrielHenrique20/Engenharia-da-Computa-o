package dao;

import aeds3.ArvoreBMais;
import aeds3.ParIntInt;
import model.Atividade;

public class AtividadeDAO {
    private ArquivoAtividade<Atividade> arqAtividade;
    // Índice 1:N: categoria -> atividades
    private ArvoreBMais<ParIntInt> ixCategoriaAtividade;

    public AtividadeDAO() throws Exception {
        arqAtividade = new ArquivoAtividade<>("Atividade", Atividade.class.getConstructor());
        ixCategoriaAtividade = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "relCategoriaAtividade.db");
    }

    public Atividade buscarAtividade(int id) throws Exception {
        return arqAtividade.read(id);
    }

    public boolean incluirAtividade(Atividade atividade) throws Exception {
        int id = arqAtividade.create(atividade);
        if (id > 0) {
            atividade.setId(id);
            if (atividade.getIdCategoria() > 0) {
                ixCategoriaAtividade.create(new ParIntInt(atividade.getIdCategoria(), id));
            }
            return true;
        }
        return false;
    }

    public boolean alterarAtividade(Atividade atividade) throws Exception {
        Atividade antigo = arqAtividade.read(atividade.getId());
        boolean ok = arqAtividade.update(atividade);
        if (ok && antigo != null) {
            int oldCat = -1;
            try { oldCat = antigo.getIdCategoria(); } catch(Exception e) { oldCat = -1; }
            int newCat = atividade.getIdCategoria();
            if (oldCat != newCat) {
                if (oldCat > 0) ixCategoriaAtividade.delete(new ParIntInt(oldCat, atividade.getId()));
                if (newCat > 0) ixCategoriaAtividade.create(new ParIntInt(newCat, atividade.getId()));
            }
        }
        return ok;
    }

    public boolean excluirAtividade(int id) throws Exception {
        Atividade a = arqAtividade.read(id);
        if (a != null && a.getIdCategoria() > 0) {
            ixCategoriaAtividade.delete(new ParIntInt(a.getIdCategoria(), id));
        }
        return arqAtividade.delete(id);
    }


    // listar atividades por categoria usando o índice 1:N
    public java.util.List<Atividade> listarPorCategoria(int categoriaId) throws Exception {
        java.util.ArrayList<Atividade> out = new java.util.ArrayList<>();
        for (ParIntInt p : ixCategoriaAtividade.read(new ParIntInt(categoriaId, -1))) {
            Atividade a = arqAtividade.read(p.get2());
            if (a != null) out.add(a);
        }
        return out;
    }
}