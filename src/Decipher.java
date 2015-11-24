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

        Integer sum = decipher.decrypt(cipherText);
        System.out.println("ascii sum:");
        System.out.println(sum);

    }

    public int decrypt(String cipher) {
        String password = guessDecryptionKey(cipher);
        if (password == null) {
            System.out.println("no key was found!!!");
            return 0;
        }
        System.out.println("password");
        System.out.println(password);

        byte[] cipherBytes = translateToBytes(cipher);
        byte[] decipherTxt = doXOREncrypt(cipherBytes, password);
        String string = new String(decipherTxt, StandardCharsets.UTF_8);
        System.out.println("decrypted text:");
        System.out.println(string);
        Set<Character> characterSet = new HashSet<>();
        int sum = 0;
        for (char c : string.toCharArray()) {
            if (!characterSet.contains(c)) {
                characterSet.add(c);
                sum += c;
            }
        }
        return sum;
    }

    private byte[] translateToBytes(String text) {
        String[] cipherText2Bytes = text.split(",");
        byte[] cipherBytes = new byte[cipherText2Bytes.length];
        for (int i = 0; i < cipherText2Bytes.length; i++) {
            cipherBytes[i] = Byte.valueOf(cipherText2Bytes[i]);
        }
        return cipherBytes;
    }

    private String guessDecryptionKey(String cipherText) {
        String[] inputArr = cipherText.split(",");
        long start = System.currentTimeMillis();
        char[] plain = "Natural".toCharArray();
        String key;
        for (int index = 0; index + 6 < inputArr.length; ++index) {
            StringBuilder current = new StringBuilder();
            for (int indexOfPlain = 0; indexOfPlain < plain.length; ++indexOfPlain) {
                char c = (char) (plain[indexOfPlain] ^ Integer.parseInt(inputArr[index + indexOfPlain]));
                current.append(c);
            }
            if (current.substring(0, 3).equals(current.substring(3, 6)) && current.charAt(0) == current.charAt(6)) {
                int shift = 3 - (index) % 3;
                key = current.substring(shift, shift + 3);
                System.out.println("find key. spent " + (System.currentTimeMillis() - start) + " ms");
                return key;
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
