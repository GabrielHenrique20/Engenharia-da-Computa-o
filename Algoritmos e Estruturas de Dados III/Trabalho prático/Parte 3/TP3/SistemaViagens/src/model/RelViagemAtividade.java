package model;

import java.io.*;

// Classe que representa a relação entre Viagem e Atividade (com atributos próprios)

// Chave primária composta: (idViagem, idAtividade)
public class RelViagemAtividade implements Registro {
    private int id; // PK do registro da relação
    private int idViagem; // FK
    private int idAtividade; // FK
    private String status; // atributo do relacionamento
    private int prioridade; // atributo do relacionamento

    public RelViagemAtividade() {
        this(-1, -1, -1, "", 0);
    }

    public RelViagemAtividade(int id, int idViagem, int idAtividade, String status, int prioridade) {
        this.id = id;
        this.idViagem = idViagem;
        this.idAtividade = idAtividade;
        this.status = status == null ? "" : status;
        this.prioridade = prioridade;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }

    // Não se aplica para este registro, retornamos -1 para cumprir a interface
    public int getIdUsuario() {
        return -1;
    }

    public int getIdViagem() {
        return idViagem;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public String getStatus() {
        return status;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setIdViagem(int v) {
        this.idViagem = v;
    }

    public void setIdAtividade(int a) {
        this.idAtividade = a;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public void setPrioridade(int p) {
        this.prioridade = p;
    }

    // Serialização e desserialização
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idViagem);
        dos.writeInt(this.idAtividade);
        byte[] bs = this.status.getBytes("UTF-8");
        dos.writeShort(bs.length);
        dos.write(bs);
        dos.writeInt(this.prioridade);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idViagem = dis.readInt();
        this.idAtividade = dis.readInt();
        short ts = dis.readShort();
        byte[] bs = new byte[ts];
        dis.readFully(bs);
        this.status = new String(bs, "UTF-8");
        this.prioridade = dis.readInt();
    }

    @Override
    public String toString() {
        return "Rel[ id=" + id + ", viagem=" + idViagem + ", atividade=" + idAtividade +
                ", status=" + status + ", prioridade=" + prioridade + " ]";
    }
}
