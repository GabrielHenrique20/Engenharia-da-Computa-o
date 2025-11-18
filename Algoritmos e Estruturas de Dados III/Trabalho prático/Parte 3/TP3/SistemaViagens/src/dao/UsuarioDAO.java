package dao;

import model.Usuario;

public class UsuarioDAO {
    private ArquivoUsuario<Usuario> arqUsuario;

    public UsuarioDAO() throws Exception {
        arqUsuario = new ArquivoUsuario<>("Usuario", Usuario.class.getConstructor());
    }

    public Usuario buscarUsuario(int id) throws Exception {
        return arqUsuario.read(id);
    }

    public boolean incluirUsuario(Usuario usuario) throws Exception {
        return arqUsuario.create(usuario) > 0;
    }

    public boolean alterarUsuario(Usuario usuario) throws Exception {
        return arqUsuario.update(usuario);
    }

    public boolean excluirUsuario(int id) throws Exception {
        // MÉTODO CASCATA: apaga viagens do usuário antes de apagar o usuário
        ViagemDAO vdao = new ViagemDAO();
        for (model.Viagem v : vdao.listarPorUsuario(id)) {
            vdao.excluirViagem(v.getId());
        }
        return arqUsuario.delete(id);
    }
}
