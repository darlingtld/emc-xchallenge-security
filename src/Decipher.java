import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Decipher {

    public static void main(String[] args) throws IOException {
        Decipher decipher = new Decipher();
        String cipherText = decipher.readFile("C:\\Users\\tangl9\\IdeaProjects\\decryption\\src\\cipher.txt");

        System.out.println("cipher text:");
        System.out.println(cipherText);

        String sum = decipher.decrypt(cipherText);
        System.out.println("ascii sum:");
        System.out.println(sum);

    }

    public String decrypt(String cipher) {
        String password = guessDecryptionKey2(cipher);
        if (password == null) {
            System.out.println("no key was found!!!");
            return null;
        }
        System.out.println("password");
        System.out.println(password);

        byte[] cipherBytes = translateToBytes(cipher);
        byte[] decipherTxt = doXOREncrypt(cipherBytes, password);
        String string = new String(decipherTxt, StandardCharsets.UTF_8);
        System.out.println("decrypted text:");
        System.out.println(string);
        Set<Character> characterSet = new HashSet<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (char c : string.toCharArray()) {
            if (!characterSet.contains(c)) {
                characterSet.add(c);
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }

    private byte[] translateToBytes(String text) {
        String[] cipherText2Bytes = text.split(",");
        byte[] cipherBytes = new byte[cipherText2Bytes.length];
        for (int i = 0; i < cipherText2Bytes.length; i++) {
            cipherBytes[i] = Byte.valueOf(cipherText2Bytes[i]);
        }
        return cipherBytes;
    }

    //avg 14.5 seconds to get the key
    private String guessDecryptionKey(String cipherText) {
        byte[] cipherTextBytes = translateToBytes(cipherText);
        long start = System.currentTimeMillis();
        byte[] key = new byte[3];
        int keylen = key.length;
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 128; z++) {
                    key[0] = Byte.valueOf(String.valueOf(x));
                    key[1] = Byte.valueOf(String.valueOf(y));
                    key[2] = Byte.valueOf(String.valueOf(z));

                    byte[] xorBytes = new byte[cipherTextBytes.length];
                    for (int i = 0; i < cipherTextBytes.length; i++) {
                        xorBytes[i] = (byte) (cipherTextBytes[i] ^ key[i % keylen]);
                    }
                    String string = new String(xorBytes, StandardCharsets.UTF_8);
                    if (string.contains("Natural")) {
                        System.out.println("Get key. spent " + (System.currentTimeMillis() - start) + " ms");
                        return new String(key, StandardCharsets.UTF_8);
                    }
                }
            }
        }
        System.out.println("Search all possible keys. spent " + (System.currentTimeMillis() - start) + " ms");
        return null;
    }

    //avg 5 seconds to get the key
    private String guessDecryptionKey2(String cipherText) {
        long start = System.currentTimeMillis();
        byte[] key = new byte[3];
        int keylen = key.length;
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 128; z++) {
                    key[0] = Byte.valueOf(String.valueOf(x));
                    key[1] = Byte.valueOf(String.valueOf(y));
                    key[2] = Byte.valueOf(String.valueOf(z));
                    byte[] naturalBytes = convertToBytes("Natural");
                    byte[] xorBytes = new byte[naturalBytes.length];
                    String xorStr = "";
                    for (int i = 0; i < naturalBytes.length; i++) {
                        xorBytes[i] = (byte) (naturalBytes[i] ^ key[i % keylen]);
                        if (i < naturalBytes.length - 1) {
                            xorStr += xorBytes[i] + ",";
                        } else {
                            xorStr += xorBytes[i];
                        }
                    }
                    if (cipherText.contains(xorStr)) {
                        System.out.println("Get key. spent " + (System.currentTimeMillis() - start) + " ms");
                        return new String(key, StandardCharsets.UTF_8);
                    }
                }
            }
        }
        System.out.println("Search all possible keys. spent " + (System.currentTimeMillis() - start) + " ms");
        return null;
    }

    private byte[] doXOREncrypt(byte[] bytes, String password) {
        int keyLen = password.length();
        byte[] key = convertToBytes(password);
        byte[] xorBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            xorBytes[i] = (byte) (bytes[i] ^ key[i % keyLen]);
        }
        return xorBytes;
    }

    private String readFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        StringBuffer text = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            text.append(line);
        }
        return text.toString();
    }

    private byte[] convertToBytes(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    private void printBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (i < bytes.length - 1) {
                System.out.print(bytes[i] + ",");
            } else {
                System.out.println(bytes[i]);
            }
        }
    }
}
