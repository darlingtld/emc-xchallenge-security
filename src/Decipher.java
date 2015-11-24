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
        String password = guessDecryptionKey(cipher);
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

    private String guessDecryptionKey(String cipherText) {
        long start = System.currentTimeMillis();
        String[] arr = cipherText.split(",");
        char[] input1 = "Nul".toCharArray();
        char[] input2 = "ar".toCharArray();
        char[] input3 = "ta".toCharArray();
        for (int firstDigit = 0; firstDigit < 128; ++firstDigit) {
            char[] output1 = new char[3];
            for (int index1 = 0; index1 < input1.length; ++index1) {
                output1[index1] = (char) (input1[index1] ^ firstDigit);
            }
            for (int n = 0; n + 6 < arr.length; ++n) {
                if (output1[0] == Integer.parseInt(arr[n]) && output1[1] == Integer.parseInt(arr[n + 3]) && output1[2] == Integer.parseInt(arr[n + 6])) {
                    for (int secondDigit = 0; secondDigit < 128; ++secondDigit) {
                        char[] output2 = new char[2];
                        for (int index2 = 0; index2 < input2.length; ++index2) {
                            output2[index2] = (char) (input2[index2] ^ secondDigit);
                        }
                        if (output2[0] == Integer.parseInt(arr[n + 1]) && output2[1] == Integer.parseInt(arr[n + 4])) {
                            for (int thirdDigit = 0; thirdDigit < 128; ++thirdDigit) {
                                char[] output3 = new char[2];
                                for (int index3 = 0; index3 < input3.length; ++index3) {
                                    output3[index3] = (char) (input3[index3] ^ thirdDigit);
                                }
                                if (output3[0] == Integer.parseInt(arr[n + 2]) && output3[1] == Integer.parseInt(arr[n + 5])) {
                                    System.out.println("Get key. spent " + (System.currentTimeMillis() - start) + " ms");
                                    return new String(new char[]{(char) firstDigit, (char) secondDigit, (char) thirdDigit});
                                }
                            }
                        }
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
