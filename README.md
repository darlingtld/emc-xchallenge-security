# emc-xchallenge-security
X-Challenge 2015 Security Question

！！Security可以个人或组队参赛，可以与Open Source项目不是同一个队。！！ 

rawedit
Question 
XOR encryption 

A big part of information security is Cryptography, and one of the key methods is encryption. 

A modern encryption method is to take a plain text input, convert the characters to ASCII, then XOR each byte with a given value, taken from a encryption key. The advantage with the XOR function is that using the same encryption key on the cipher text, restores the plain text; for example, 65 XOR 112 = 49, then 112 XOR 49 = 65. 

For unbreakable encryption, the key is the same length as the plain text message, and the key is made up of random bytes. The user would keep the encrypted message and the encryption key in different locations, and without both "halves", it is impossible to decrypt the message. 

Unfortunately, this method is impractical, so the modified method is to use a password as the encryption key. If the password is shorter than the message, which is likely, the key is repeated cyclically throughout the message. The balance for this method is using a sufficiently long password key for security, but short enough to be memorable. 

Your task is decrypt the cipher text provided without knowing the password, return the sum of unique character’s ASCII value in the plain text. 

Some information already known: 
1. The password length is three characters. 
2. In the plain text, there is a word we already known: Natural 

 Sample Case 

• plain.txt: Plain.txt 
• cipher.txt: Cipher.txt 

上述文件中包含了原文件字符和加密后的文件。测试用例时，只提供加密后的文件，输出结果要求是原文件不同的字符的ASCII值的总和。
