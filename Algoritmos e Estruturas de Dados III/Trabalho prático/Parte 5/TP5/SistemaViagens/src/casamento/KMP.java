package casamento;

import java.util.ArrayList;

/**
 * @author GeeksforGeeks
 *         Alteramos um pouco dele tambem para melhor funcionamento no sistema
 */

public class KMP {
    public static ArrayList<Integer> computeLPSArray(String pattern) {
        int n = pattern.length();
        ArrayList<Integer> lps = new ArrayList<>();
        for (int k = 0; k < n; k++)
            lps.add(0);

        
        int len = 0;
        int i = 1;

        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps.set(i, len);
                i++;
            } else {
                if (len != 0) {
                    
                    len = lps.get(len - 1);
                } else {
                    lps.set(i, 0);
                    i++;
                }
            }
        }

        return lps;
    }

    // Busca KMP completa: retorna índice da primeira ocorrência de pattern em text,
    // ou -1
    public static int search(String text, String pattern) {
        if (text == null || pattern == null)
            return -1;
        int n = text.length();
        int m = pattern.length();
        if (m == 0)
            return 0;
        if (m > n)
            return -1;

        java.util.ArrayList<Integer> lps = computeLPSArray(pattern);
        int i = 0; // índice em texto
        int j = 0; // índice em padrão

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    return i - j; // casamento encontrado
                }
            } else {
                if (j != 0) {
                    j = lps.get(j - 1);
                } else {
                    i++;
                }
            }
        }

        return -1;
    }

    // Retorna true se o padrão ocorrer pelo menos uma vez no texto (se der match)
    public static boolean match(String text, String pattern) {
        return search(text, pattern) != -1;
    }
}
