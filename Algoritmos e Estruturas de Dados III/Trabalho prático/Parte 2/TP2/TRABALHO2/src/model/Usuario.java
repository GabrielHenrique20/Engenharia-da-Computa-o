package model;
import java.io.*;

public class Usuario implements RegistroUsuario {
    private int id; // PK
    private String nome;
    private String[] telefone;
    private String email;

    // Construtores
    public Usuario() {
        this(-1, "", null, "");
    }

    public Usuario(String nome, String[] telefone, String email) {
        this(0, nome, telefone, email); // ID = 0 (será sobrescrito pelo DAO)
    }

    public Usuario(int id, String nome, String[] telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
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

    public String[] getTelefone() {
        return telefone;
    }

    public void setTelefone(String[] telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        // telefone
        if (this.telefone != null) {
            dos.writeInt(this.telefone.length);
            for (String tel : this.telefone) {
                byte[] bytesTel = tel.getBytes("UTF-8");
                dos.writeShort(bytesTel.length);
                dos.write(bytesTel);
            }
        } else {
            dos.writeInt(0); // nenhum telefone
        }

        // email
        byte[] bytesT = this.email.getBytes("UTF-8");
        dos.writeShort(bytesT.length);
        dos.write(bytesT);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();

        // nome
        short tamNome = dis.readShort();
        byte[] bytesNome = new byte[tamNome];
        dis.readFully(bytesNome);
        this.nome = new String(bytesNome, "UTF-8");

        // telefones
        int qtd = dis.readInt();
        this.telefone = new String[qtd];
        for (int i = 0; i < qtd; i++) {
            short tamTel = dis.readShort();
            byte[] bytesTel = new byte[tamTel];
            dis.readFully(bytesTel);
            this.telefone[i] = new String(bytesTel, "UTF-8");
        }

        // email
        short tamEmail = dis.readShort();
        byte[] bytesEmail = new byte[tamEmail];
        dis.readFully(bytesEmail);
        this.email = new String(bytesEmail, "UTF-8");
    }

    @Override
    public String toString() {
        String telefonesStr = "";
        if (this.telefone != null) {
            telefonesStr = String.join(", ", this.telefone);
        }
    
        return "\nID........: " + this.id +
               "\nNome......: " + this.nome +
               "\nTelefone..: " + telefonesStr +
               "\nEmail.....: " + this.email;
    }
    
}
