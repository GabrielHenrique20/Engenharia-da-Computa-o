package casamento;

/**
 * @author GeeksforGeeks
 *         Alteramos um pouco dele tambem para melhor funcionamento no sistema
 */

public class BoyerMoore {

    static int NO_OF_CHARS = 65536; // vai pegar o numero de caracteres Unicode (UTF-16)

    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    static void badCharHeuristic(char[] str, int size,
            int badchar[]) {

        // Initialize all occurrences as -1
        for (int i = 0; i < NO_OF_CHARS; i++)
            badchar[i] = -1;

        for (int i = 0; i < size; i++)
            badchar[(int) str[i]] = i;
    }

    static void search(char txt[], char pat[]) {
        int m = pat.length;
        int n = txt.length;

        int badchar[] = new int[NO_OF_CHARS];

        badCharHeuristic(pat, m, badchar);

        int s = 0;

        while (s <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && pat[j] == txt[s + j])
                j--;

            if (j < 0) {
                System.out.println(
                        "Patterns occur at shift = " + s);

                s += (s + m < n) ? m - badchar[txt[s + m]]
                        : 1;
            }

            else

                s += max(1, j - badchar[txt[s + j]]);
        }
    }

    // Versão que retorna o índice da primeira ocorrência de padrão em txt (ou -1)
    public static int indexOf(String txt, String pat) {
        if (txt == null || pat == null)
            return -1;
        int n = txt.length();
        int m = pat.length();
        if (m == 0)
            return 0;
        if (m > n)
            return -1;

        int[] badchar = new int[NO_OF_CHARS];
        badCharHeuristic(pat.toCharArray(), m, badchar);

        int s = 0; 
        while (s <= (n - m)) {
            int j = m - 1;

            // anda do fim do padrão para o início enquanto os caracteres casam
            while (j >= 0 && pat.charAt(j) == txt.charAt(s + j))
                j--;

            if (j < 0) {
                return s; // casamento encontrado
            } else {
                s += max(1, j - badchar[txt.charAt(s + j)]);
            }
        }

        return -1;
    }

    // Retorna true se o padrão ocorrer pelo menos uma vez no texto
    public static boolean match(String txt, String pat) {
        return indexOf(txt, pat) != -1;
    }
}
