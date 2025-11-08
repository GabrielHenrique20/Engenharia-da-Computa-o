
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BuscarAtividade {

    public static void main(String[] args) {
        try {
            analisarArquivo("dados/Atividade/Atividade.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analisarArquivo(String nomeArquivo) throws Exception {
        try (RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "r")) {

            System.out.println("\n=== REGISTROS ===");
            arquivo.seek(12);
            int reg = 1;

            while (arquivo.getFilePointer() < arquivo.length()) {
                byte lapide = arquivo.readByte();
                short tamanho = arquivo.readShort();
                byte[] dados = new byte[tamanho];
                arquivo.readFully(dados);

                System.out.println("\n--- REGISTRO " + reg + " ---");
                System.out.println("Status: " + (lapide == ' ' ? "ATIVO" : "EXCLUÍDO"));
                // System.out.println("Tamanho: " + tamanho + " bytes");

                if (lapide == ' ') {
                    decodeAtividade(dados);
                }
                reg++;
            }
        }
    }

    private static void decodeAtividade(byte[] dados) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dados));

        int id = dis.readInt();

        // título
        short tamTitulo = dis.readShort();
        byte[] bytesTitulo = new byte[tamTitulo];
        dis.readFully(bytesTitulo);
        String titulo = new String(bytesTitulo, "UTF-8");

        // descrição
        short tamDescricao = dis.readShort();
        byte[] bytesDescricao = new byte[tamDescricao];
        dis.readFully(bytesDescricao);
        String descricao = new String(bytesDescricao, "UTF-8");

        // custo
        float custo = dis.readFloat();

        // data
        LocalDate data = LocalDate.ofEpochDay(dis.readLong());

        System.out.println("ID........: " + id);
        System.out.println("Título....: " + titulo);
        System.out.println("Descrição.: " + descricao);
        System.out.println("Custo.....: " + custo);
        System.out.println("Data......: " + data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}

// java -cp out src.BuscarAtividade