
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;

public class BuscarCategoria {

    public static void main(String[] args) {
        try {
            analisarArquivo("dados/Categoria/Categoria.db");
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
        short tamNome = dis.readShort();
        byte[] bytesNome = new byte[tamNome];
        dis.readFully(bytesNome);
        String nome = new String(bytesNome, "UTF-8");

        System.out.println("ID........: " + id);
        System.out.println("Nome....: " + nome);
    }
}

// java -cp out src.BuscarAtividade