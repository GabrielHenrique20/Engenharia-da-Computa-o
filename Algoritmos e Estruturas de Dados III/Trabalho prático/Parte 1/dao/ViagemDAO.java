package dao;

import model.Viagem;

public class ViagemDAO {
    private Arquivo<Viagem> arqViagem;

    public ViagemDAO() throws Exception {
        arqViagem = new Arquivo<>("Viagem", Viagem.class.getConstructor());
    }

    public Viagem buscarViagem(int id) throws Exception {
        return arqViagem.read(id);
    }

    public boolean incluirViagem(Viagem viagem) throws Exception {
        return arqViagem.create(viagem) > 0;
    }

    public boolean alterarViagem(Viagem viagem) throws Exception {
        return arqViagem.update(viagem);
    }

    public boolean excluirViagem(int id) throws Exception {
        return arqViagem.delete(id);
    }
}
