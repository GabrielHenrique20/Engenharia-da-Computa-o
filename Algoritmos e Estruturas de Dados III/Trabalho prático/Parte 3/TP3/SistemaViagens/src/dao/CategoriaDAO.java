package dao;
import model.Categoria;
import java.util.List;

@SuppressWarnings("unused")
public class CategoriaDAO {
    private ArquivoCategoria<Categoria> arqCategoria;

    public CategoriaDAO() throws Exception {
        arqCategoria = new ArquivoCategoria<>("Categoria", Categoria.class.getConstructor());
    }

    public Categoria buscarCategoria(int id) throws Exception {
        return arqCategoria.read(id);
    }

    public boolean incluirCategoria(Categoria categoria) throws Exception {
        return arqCategoria.create(categoria) > 0;
    }

    public boolean alterarCategoria(Categoria categoria) throws Exception {
        return arqCategoria.update(categoria);
    }

    public boolean excluirCategoria(int id) throws Exception {
        // CASCATA: apagar atividades vinculadas à categoria antes
        AtividadeDAO adao = new AtividadeDAO();
        try {
            for (model.Atividade a : adao.listarPorCategoria(id)) {
                adao.excluirAtividade(a.getId());
            }
        } catch(Exception e) {
            // segue mesmo assim; mas ideal é tratar/logar
        }
        return arqCategoria.delete(id);
    }
}
