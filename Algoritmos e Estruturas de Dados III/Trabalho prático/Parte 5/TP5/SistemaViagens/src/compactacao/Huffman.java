package compactacao;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.PriorityQueue;

// Nó da árvore de Huffman
// Usado internamente na construção da árvore
// e geração dos códigos
// para cada byte da sequência.
// Implementa Comparable para uso na PriorityQueue
// baseada na frequência dos bytes.
/**
 * @author Marcos Kutova
 *         PUC Minas
 * Alteramos um pouco dele tambem para melhor funcionamento no sistema
 */

class HuffmanNode implements Comparable<HuffmanNode> {
    byte b;
    int frequencia;
    HuffmanNode esquerdo, direito;

    public HuffmanNode(byte b, int f) {
        this.b = b;
        this.frequencia = f;
        esquerdo = direito = null;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.frequencia - o.frequencia;
    }
}

public class Huffman {

    public static HashMap<Byte, String> codifica(byte[] sequencia) {
        HashMap<Byte, Integer> mapaDeFrequencias = new HashMap<>();
        for (byte c : sequencia) {
            mapaDeFrequencias.put(c, mapaDeFrequencias.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Byte b : mapaDeFrequencias.keySet()) {
            pq.add(new HuffmanNode(b, mapaDeFrequencias.get(b)));
        }

        while (pq.size() > 1) {
            HuffmanNode esquerdo = pq.poll();
            HuffmanNode direito = pq.poll();

            HuffmanNode pai = new HuffmanNode((byte) 0, esquerdo.frequencia + direito.frequencia);
            pai.esquerdo = esquerdo;
            pai.direito = direito;

            pq.add(pai);
        }

        HuffmanNode raiz = pq.poll();
        HashMap<Byte, String> codigos = new HashMap<>();
        constroiCodigos(raiz, "", codigos);

        return codigos;
    }

    private static void constroiCodigos(HuffmanNode no, String codigo, HashMap<Byte, String> codigos) {
        if (no == null) {
            return;
        }

        // ignorava o byte 0 (alteramos aqui).
        // Nó folha: ambos os filhos são nulos. Isso permite códigos para qualquer byte,
        // incluindo o valor 0.
        if (no.esquerdo == null && no.direito == null) {
            codigos.put(no.b, codigo);
        }

        constroiCodigos(no.esquerdo, codigo + "0", codigos);
        constroiCodigos(no.direito, codigo + "1", codigos);
    }

    // Versão buscando na tabela de códigos.
    public static byte[] decodifica(String sequenciaCodificada, HashMap<Byte, String> codigos) {
        ByteArrayOutputStream sequenciaDecodificada = new ByteArrayOutputStream();
        StringBuilder codigoAtual = new StringBuilder();

        for (int i = 0; i < sequenciaCodificada.length(); i++) {
            codigoAtual.append(sequenciaCodificada.charAt(i));
            for (byte b : codigos.keySet()) {
                if (codigos.get(b).equals(codigoAtual.toString())) {
                    sequenciaDecodificada.write(b);
                    codigoAtual = new StringBuilder();
                    break;
                }
            }
        }
        return sequenciaDecodificada.toByteArray();
    }
}
