
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;

public class BuscarUsuario {
    
    public static void main(String[] args) {
        try {
            analisarArquivo("dados/Usuario/Usuario.db");
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
                //System.out.println("Tamanho: " + tamanho + " bytes");
                
                if (lapide == ' ') {
                    decodeUsuario(dados);
                }
                reg++;
            }
        }
    }
    
    private static void decodeUsuario(byte[] dados) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dados));
        
        System.out.println("ID: " + dis.readInt());
        
        short tamNome = dis.readShort();
        byte[] bytesNome = new byte[tamNome];
        dis.readFully(bytesNome);
        System.out.println("Nome: " + new String(bytesNome, "UTF-8"));
        
        int qtd = dis.readInt();
        for (int i = 0; i < qtd; i++) {
            short tamTel = dis.readShort();
            byte[] bytesTel = new byte[tamTel];
            dis.readFully(bytesTel);
            System.out.println("Telefone[" + (i + 1) + "]: " + new String(bytesTel, "UTF-8"));
        }        
    
        short tamEmail = dis.readShort();
        byte[] bytesEmail = new byte[tamEmail];
        dis.readFully(bytesEmail);
        System.out.println("Email: " + new String(bytesEmail, "UTF-8"));
    }
}

//java -cp out src.BuscarUsuario  