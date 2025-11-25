
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BuscarViagem {
    
    public static void main(String[] args) {
        try {
            analisarArquivo("dados/Viagem/Viagem.db");
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
                    decodeViagem(dados);
                }
                reg++;
            }
        }
    }
    
    private static void decodeViagem(byte[] dados) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dados));
        
        System.out.println("ID: " + dis.readInt());
        
        short tamDestino = dis.readShort();
        byte[] bytesDestino = new byte[tamDestino];
        dis.readFully(bytesDestino);
        System.out.println("Destino: " + new String(bytesDestino));
        
        System.out.println("Orçamento: " + dis.readFloat());
        
        LocalDate inicio = LocalDate.ofEpochDay(dis.readLong());
        LocalDate fim = LocalDate.ofEpochDay(dis.readLong());
        
        System.out.println("Data Início: " + inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data Fim: " + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}

