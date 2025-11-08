package model;
import java.io.*;

public class Categoria implements RegistroCategoria {
    private int id; // PK
    private String nome;

    // Construtores
    public Categoria() {
        this(-1, "");
    }

    public Categoria(String nome) {
        this(0, nome); // ID = 0 (será sobrescrito pelo DAO)
    }

    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Serialização
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);

        // nome
        byte[] bytesNome = this.nome.getBytes("UTF-8");
        dos.writeShort(bytesNome.length);
        dos.write(bytesNome);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();

        // título
        short tamNome = dis.readShort();
        byte[] bytesNome = new byte[tamNome];
        dis.readFully(bytesNome);
        this.nome = new String(bytesNome, "UTF-8");

    }

    @Override
    public String toString() {
        return "\nID........: " + this.id +
               "\nTítulo....: " + this.nome;
    }
}
