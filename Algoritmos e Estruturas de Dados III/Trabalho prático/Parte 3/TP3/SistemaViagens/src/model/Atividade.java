package model;
import java.io.*;
import java.time.LocalDate;

public class Atividade implements RegistroAtividade {
    private int id; // PK
    private int idCategoria; // FK: Categoria -> 1:N Atividade
    private String titulo;
    private String descricao;
    private float custo;
    private LocalDate data;

    // Construtores
    public Atividade() {
        this(-1, -1, "", "", 0, LocalDate.now());
    }

    public Atividade(String titulo, String descricao, float custo, LocalDate data) {
        this(0, -1, titulo, descricao, custo, data); // ID = 0 (será sobrescrito pelo DAO)
    }

    public Atividade(int id, int idCategoria, String titulo, String descricao, float custo, LocalDate data) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.custo = custo;
        this.data = data;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // Serialização
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);

        // título
        byte[] bytesTitulo = this.titulo.getBytes("UTF-8");
        dos.writeShort(bytesTitulo.length);
        dos.write(bytesTitulo);

        // descrição
        byte[] bytesDescricao = this.descricao.getBytes("UTF-8");
        dos.writeShort(bytesDescricao.length);
        dos.write(bytesDescricao);

        // custo
        dos.writeFloat(this.custo);

        // data
        dos.writeLong(this.data.toEpochDay());

        dos.writeInt(this.idCategoria);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();

        // título
        short tamTitulo = dis.readShort();
        byte[] bytesTitulo = new byte[tamTitulo];
        dis.readFully(bytesTitulo);
        this.titulo = new String(bytesTitulo, "UTF-8");

        // descrição
        short tamDescricao = dis.readShort();
        byte[] bytesDescricao = new byte[tamDescricao];
        dis.readFully(bytesDescricao);
        this.descricao = new String(bytesDescricao, "UTF-8");

        // custo
        this.custo = dis.readFloat();

        // data
        this.data = LocalDate.ofEpochDay(dis.readLong());
        // tentar ler idCategoria (campo opcional para compatibilidade)
        try {
            this.idCategoria = dis.readInt();
        } catch (Exception eof) {
            this.idCategoria = -1;
        }
    }

    @Override
    public String toString() {
        return "\nID........: " + this.id +
               "\nTítulo....: " + this.titulo +
               "\nDescrição.: " + this.descricao +
               "\nCusto.....: " + this.custo +
               "\nData......: " + this.data;
    }
}
