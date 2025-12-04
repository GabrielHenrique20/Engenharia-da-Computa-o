package controller;

import java.util.Scanner;
import compactacao.Backup;

// Menu de backup e restauração

public class MenuBackup {

    private Scanner console = new Scanner(System.in);

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Backup / Compactação");
            System.out.println("1 - COMPACTAR EM HUFFMAN");
            System.out.println("2 - DESCOMPACTAR EM HUFFMAN");
            System.out.println("3 - COMPACTAR EM LZW");
            System.out.println("4 - DESCOMPACTAR EM LZW");
            System.out.println("0 - Voltar");
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    try {
                        Backup.gerarBackupHuffman();
                        System.out.println("Arquivo compactado em Huffman gerado em '" + Backup.BACKUP_HUFFMAN + "'.");
                    } catch (Exception e) {
                        System.out.println("Erro ao gerar backup Huffman: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Backup.restaurarBackupHuffman();
                        System.out.println("Arquivos descompactados a partir do backup Huffman.");
                    } catch (Exception e) {
                        System.out.println("Erro ao restaurar backup Huffman: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Backup.gerarBackupLZW();
                        System.out.println("Arquivo compactado em LZW gerado em '" + Backup.BACKUP_LZW + "'.");
                    } catch (Exception e) {
                        System.out.println("Erro ao gerar backup LZW: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        Backup.restaurarBackupLZW();
                        System.out.println("Arquivos descompactados a partir do backup LZW.");
                    } catch (Exception e) {
                        System.out.println("Erro ao restaurar backup LZW: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
