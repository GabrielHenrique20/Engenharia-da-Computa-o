package compactacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe para geração e restauração de backups compactados dos arquivos de dados do sistema

public class Backup {

    public static final String BACKUP_HUFFMAN = "backup_huffman.db";
    public static final String BACKUP_LZW = "backup_lzw.db";

    // Gera backup usando Huffman
    public static void gerarBackupHuffman() throws Exception {
        byte[] imagem = construirImagemBackup();
        if (imagem == null || imagem.length == 0) {
            System.out.println("Nenhum arquivo de dados encontrado para backup.");
            return;
        }

        // Codifica com Huffman
        HashMap<Byte, String> codigos = Huffman.codifica(imagem);

        VetorDeBits vb = new VetorDeBits();
        int pos = 0;
        for (byte b : imagem) {
            String code = codigos.get(b);
            if (code == null)
                continue;
            for (int i = 0; i < code.length(); i++) {
                char c = code.charAt(i);
                if (c == '1')
                    vb.set(pos);
                // se for '0' deixamos o bit desligado
                pos++;
            }
        }
        byte[] bitsBytes = vb.toByteArray();

        // Grava arquivo de backup com cabeçalho simples (tabela de códigos + vetor de
        // bits)
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(BACKUP_HUFFMAN))) {
            out.writeInt(1); // versão
            // tabela de códigos
            out.writeInt(codigos.size());
            for (Map.Entry<Byte, String> e : codigos.entrySet()) {
                out.writeByte(e.getKey());
                out.writeInt(e.getValue().length());
                out.writeBytes(e.getValue()); // '0' e '1'
            }
            // vetor de bits codificado
            out.writeInt(bitsBytes.length);
            out.write(bitsBytes);
        }
    }

    // Restaura backup gerado por Huffman
    public static void restaurarBackupHuffman() throws Exception {
        File f = new File(BACKUP_HUFFMAN);
        if (!f.exists()) {
            System.out.println("Arquivo de backup Huffman não encontrado.");
            return;
        }
        byte[] imagem;
        try (DataInputStream in = new DataInputStream(new FileInputStream(f))) {
            int versao = in.readInt();
            if (versao != 1) {
                throw new IOException("Versão de backup Huffman desconhecida: " + versao);
            }
            int mapSize = in.readInt();
            HashMap<Byte, String> codigos = new HashMap<>();
            for (int i = 0; i < mapSize; i++) {
                byte key = in.readByte(); 
                int len = in.readInt();
                byte[] codeBytes = new byte[len];
                in.readFully(codeBytes);
                String code = new String(codeBytes, "UTF-8");
                codigos.put(key, code);
            }
            int bitsLen = in.readInt();
            byte[] bitsBytes = new byte[bitsLen];
            in.readFully(bitsBytes);
            VetorDeBits vb = new VetorDeBits(bitsBytes);
            String sequenciaCodificada = vb.toString();
            imagem = Huffman.decodifica(sequenciaCodificada, codigos);
        }
        reconstruirArquivos(imagem);
    }

    // -------------------------------------------------------------------------------------------//

    // Gera backup usando LZW
    public static void gerarBackupLZW() throws Exception {
        byte[] imagem = construirImagemBackup();
        if (imagem == null || imagem.length == 0) {
            System.out.println("Nenhum arquivo de dados encontrado para backup.");
            return;
        }
        byte[] codificada = LZW.codifica(imagem);
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(BACKUP_LZW))) {
            out.writeInt(1); // versão
            out.writeInt(codificada.length);
            out.write(codificada);
        }
    }

    // Restaura backup LZW
    public static void restaurarBackupLZW() throws Exception {
        File f = new File(BACKUP_LZW);
        if (!f.exists()) {
            System.out.println("Arquivo de backup LZW não encontrado.");
            return;
        }
        byte[] codificada;
        try (DataInputStream in = new DataInputStream(new FileInputStream(f))) {
            int versao = in.readInt();
            if (versao != 1) {
                throw new IOException("Versão de backup LZW desconhecida: " + versao);
            }
            int tam = in.readInt();
            codificada = new byte[tam];
            in.readFully(codificada);
        }
        byte[] imagem = LZW.decodifica(codificada);
        reconstruirArquivos(imagem);
    }

    // -------------------------------------------------------------------------------------------//

    // Monta um único vetor de bytes com todos os arquivos de dados utilizados pelo
    // sistema (ate os da relacoes)
    private static byte[] construirImagemBackup() throws IOException {
        List<File> arquivos = listarArquivosDados();
        if (arquivos.isEmpty())
            return new byte[0];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        // versão do formato de imagem
        out.writeInt(1);
        out.writeInt(arquivos.size());

        byte[] buffer = new byte[8192];
        for (File f : arquivos) {
            String relPath = getRelativePath(f);
            byte[] pathBytes = relPath.getBytes("UTF-8");
            out.writeInt(pathBytes.length);
            out.write(pathBytes);
            long len = f.length();
            out.writeLong(len);

            try (FileInputStream fis = new FileInputStream(f)) {
                long restante = len;
                while (restante > 0) {
                    int l = fis.read(buffer, 0, (int) Math.min(restante, buffer.length));
                    if (l == -1)
                        break;
                    out.write(buffer, 0, l);
                    restante -= l;
                }
            }
        }
        out.flush();
        return baos.toByteArray();
    }

    // Reconstrói os arquivos de dados a partir da imagem de backup
    private static void reconstruirArquivos(byte[] imagem) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imagem);
        DataInputStream in = new DataInputStream(bais);

        int versao = in.readInt();
        if (versao != 1) {
            throw new IOException("Versão de imagem de backup desconhecida: " + versao);
        }

        int qtdArquivos = in.readInt();
        byte[] buffer = new byte[8192];
        for (int i = 0; i < qtdArquivos; i++) {
            int pathLen = in.readInt();
            byte[] pathBytes = new byte[pathLen];
            in.readFully(pathBytes);
            String relPath = new String(pathBytes, "UTF-8");
            long len = in.readLong();

            File destino = new File(relPath);
            File dir = destino.getParentFile();
            if (dir != null && !dir.exists())
                dir.mkdirs();

            try (FileOutputStream fos = new FileOutputStream(destino)) {
                long restante = len;
                while (restante > 0) {
                    int l = in.read(buffer, 0, (int) Math.min(restante, buffer.length));
                    if (l == -1)
                        break;
                    fos.write(buffer, 0, l);
                    restante -= l;
                }
            }
        }
    }

    // Lista todos os arquivos de dados usados pelo aplicativo
    private static List<File> listarArquivosDados() {
        List<File> lista = new ArrayList<>();

        // 1) Todos os arquivos dentro de ./dados/
        File dadosDir = new File("dados");
        if (dadosDir.exists() && dadosDir.isDirectory()) {
            adicionarRecursivo(lista, dadosDir);
        }

        // 2) Arquivos .db na pasta raiz (índices das relações, etc)
        File raiz = new File(".");
        File[] raizFiles = raiz.listFiles();
        if (raizFiles != null) {
            for (File f : raizFiles) {
                String nome = f.getName();
                if (f.isFile() && nome.endsWith(".db")
                        && !nome.equals(BACKUP_HUFFMAN)
                        && !nome.equals(BACKUP_LZW)) {
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    // Adiciona arquivos recursivamente à lista
    // Se for pasta, adiciona todos os arquivos filhos
    private static void adicionarRecursivo(List<File> lista, File f) {
        if (f.isFile()) {
            lista.add(f);
        } else {
            File[] filhos = f.listFiles();
            if (filhos != null) {
                for (File c : filhos) {
                    adicionarRecursivo(lista, c);
                }
            }
        }
    }

    // Ajusta o caminho para ficar numa forma padronizada (barra /, sem ./ na frente).
    private static String getRelativePath(File f) throws IOException {
        String path = f.getPath().replace('\\', '/');
        if (path.startsWith("./")) {
            path = path.substring(2);
        }
        return path;
    }
}
