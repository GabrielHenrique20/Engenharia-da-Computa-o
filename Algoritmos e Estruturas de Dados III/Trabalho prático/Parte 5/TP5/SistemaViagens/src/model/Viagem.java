package model;
import java.io.*;
import java.time.LocalDate;

public class Viagem implements Registro {
    private int id; // PK
    private int idUsuario;  // FK: Usuario -> 1:N Viagem
    private String destino;
    private float orcamento;
    private LocalDate data_inicio;
    private LocalDate data_fim;

    // Atualizar construtores
    public Viagem() {
        this(-1, -1, "", 0, LocalDate.now(), LocalDate.now());
    }

    public Viagem(String destino, float orcamento, LocalDate data_inicio, LocalDate data_fim) {
        this(0, -1, destino, orcamento, data_inicio, data_fim);
    }

    public Viagem(int id, int idUsuario, String destino, float orcamento, LocalDate data_inicio, LocalDate data_fim) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.destino = destino;
        this.orcamento = orcamento;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Float getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(float orcamento) {
        this.orcamento = orcamento;
    }

    public LocalDate getDataInicio() {
        return data_inicio;
    }

    public void setDataInicio(LocalDate data_inicio) {
        this.data_inicio = data_inicio;
    }

    public LocalDate getDataFim() {
        return data_fim;
    }

    public void setDataFim(LocalDate data_fim) {
        this.data_fim = data_fim;
    }

    /*
     * public byte[] toByteArray() throws IOException {
     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
     * DataOutputStream dos = new DataOutputStream(baos);
     * 
     * dos.writeInt(this.id);
     * 
     * dos.writeShort(this.destino.length());
     * dos.writeBytes(this.destino);
     * 
     * dos.writeFloat(this.orcamento);
     * dos.writeLong(this.data_inicio.toEpochDay());
     * dos.writeLong(this.data_fim.toEpochDay());
     * 
     * return baos.toByteArray();
     * }
     */

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(this.idUsuario); // Adicionar idUsuario
        dos.writeShort(this.destino.length());
        dos.writeBytes(this.destino);
        dos.writeFloat(this.orcamento);
        dos.writeLong(this.data_inicio.toEpochDay());
        dos.writeLong(this.data_fim.toEpochDay());

        return baos.toByteArray();
    }

    /*
     * public void fromByteArray(byte[] b) throws IOException {
     * ByteArrayInputStream bais = new ByteArrayInputStream(b);
     * DataInputStream dis = new DataInputStream(bais);
     * 
     * this.id = dis.readInt();
     * 
     * short tamDestino = dis.readShort();
     * byte[] bytesDestino = new byte[tamDestino];
     * dis.readFully(bytesDestino);
     * this.destino = new String(bytesDestino);
     * 
     * this.orcamento = dis.readFloat();
     * this.data_inicio = LocalDate.ofEpochDay(dis.readLong());
     * this.data_fim = LocalDate.ofEpochDay(dis.readLong());
     * }
     */

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.idUsuario = dis.readInt(); // Ler idUsuario
        short tamDestino = dis.readShort();
        byte[] bytesDestino = new byte[tamDestino];
        dis.readFully(bytesDestino);
        this.destino = new String(bytesDestino);
        this.orcamento = dis.readFloat();
        this.data_inicio = LocalDate.ofEpochDay(dis.readLong());
        this.data_fim = LocalDate.ofEpochDay(dis.readLong());
    }

    @Override
    public String toString() {
        return "\nID........: " + this.id +
                "\nDestino......: " + this.destino +
                "\nOrçamento.......: " + this.orcamento +
                "\nData Início...: " + this.data_inicio +
                "\nData Fim...: " + this.data_fim;
    }
}
