package criptografia;

// Java Program for implementation of RSA Algorithm

/*RSA(Rivest-Shamir-Umdleman) Algoritmo é um assimétrico ou criptografia de chave pública algoritmo, o que significa que funciona em duas chaves diferentes: 
Chave pública e Chave Privada. A Chave Pública é usada para criptografia e é conhecido por todos, enquanto a Chave Privada é usada para descriptografia e 
deve ser mantido em segredo pelo destinatário.
O RSA é amplamente utilizado para proteger dados sensíveis, especialmente quando os dados são transmitidos pela Internet. 
/**
  * @author Geeks for Geeks
*/

import java.math.BigInteger;

public class RSA {

    // Function to compute base^expo mod m using BigInteger
    static BigInteger power(BigInteger base, BigInteger expo, BigInteger m) {
        return base.modPow(expo, m);
    }

    // Function to find modular inverse of e modulo phi(n)
    static BigInteger modInverse(BigInteger e, BigInteger phi) {
        return e.modInverse(phi);
    }

    // RSA Key Generation
    static void generateKeys(BigInteger[] keys) {
        BigInteger p = new BigInteger("7919");
        BigInteger q = new BigInteger("1009");

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e, where 1 < e < phi(n) and gcd(e, phi(n)) == 1
        BigInteger e = BigInteger.ZERO;
        for (e = new BigInteger("2"); e.compareTo(phi) < 0; e = e.add(BigInteger.ONE)) {
            if (e.gcd(phi).equals(BigInteger.ONE)) {
                break;
            }
        }

        // Compute d such that e * d ≡ 1 (mod phi(n))
        BigInteger d = modInverse(e, phi);

        keys[0] = e; // Public Key (e)
        keys[1] = d; // Private Key (d)
        keys[2] = n; // Modulus (n)
    }

    // Encrypt message using public key (e, n)
    static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
        return power(m, e, n);
    }

    // Decrypt message using private key (d, n)
    static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
        return power(c, d, n);
    }

    // Deixar as chaves fixas durante a execução do programa
    private static BigInteger publicE;
    private static BigInteger privateD;
    private static BigInteger modulusN;

    static {
        // Gera as chaves uma única vez, com p e q fixos (ver generateKeys)
        BigInteger[] keys = new BigInteger[3];
        generateKeys(keys);
        publicE = keys[0];
        privateD = keys[1];
        modulusN = keys[2];
    }

    /**
     * Criptografa uma String (por exemplo, o e-mail do usuário) usando RSA,
     * retornando uma sequência de inteiros em decimal separados por espaço.
     * Cada caractere é tratado como um byte e cifrado individualmente.
     */

    /*
     * Cada byte do texto (e-mail) é tratado como um m em [0..255].
     * 
     * É cifrado com c = m^e mod n e armazenado como decimal (string).
     * 
     * A saída é tipo: "12345 67890 43210, e assim vai".
     * 
     * Na leitura, fazemos o inverso (c^d mod n → m) e reconstruímos o texto
     * original.
     */
    public static String encryptString(String texto) {
        if (texto == null || texto.isEmpty())
            return "";
        try {
            byte[] bytes = texto.getBytes("UTF-8");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                int unsigned = bytes[i] & 0xFF; // garante valor positivo 0..255
                BigInteger m = BigInteger.valueOf(unsigned);
                BigInteger c = encrypt(m, publicE, modulusN);
                sb.append(c.toString());
                if (i < bytes.length - 1)
                    sb.append(" ");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar string com RSA", e);
        }
    }

    /**
     * Descriptografa a String produzida acima restaurando o texto
     * original.
     */
    public static String decryptString(String cifra) {
        if (cifra == null || cifra.isEmpty())
            return "";
        try {
            String[] partes = cifra.split(" ");
            byte[] bytes = new byte[partes.length];
            for (int i = 0; i < partes.length; i++) {
                if (partes[i].isEmpty()) {
                    bytes[i] = 0;
                    continue;
                }
                BigInteger c = new BigInteger(partes[i]);
                BigInteger m = decrypt(c, privateD, modulusN);
                bytes[i] = (byte) m.intValue();
            }
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar string com RSA", e);
        }
    }
}
