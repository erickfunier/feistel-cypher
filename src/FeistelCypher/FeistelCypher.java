
package FeistelCypher;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeistelCypher {
    public static void main(String[] args) {
        
        // Input Text and Keys
        String input = "Integer non vestibulum turpis, ac pulvinar lectus. Fusce ultricies at purus ac lacinia.";
        String key1 = "ufabctopper";
        String key2 = "reppotcbafu";
        
        System.out.println("Input: " + input);
        
        Results encryptedInput = encrypt(input, key1, key2);
        System.out.println("Encrypted input: " + encryptedInput.getTextEncrypted());
        
        String entradaDecifrada = decrypt(encryptedInput.getListByteEncrypted(), key1, key2);
        System.out.println("Decrypted input: " + entradaDecifrada);        
    }
    
    public static Results encrypt(String input, String key1, String key2) {
        String cipherOutput = "";
        List<byte[]> listaBytesCifrados = new ArrayList<>();
        try {
            byte[] utf8Text = input.getBytes("UTF8");
            byte[] utf8Key1 = key1.getBytes("UTF8");
            byte[] utf8Key2 = key2.getBytes("UTF8");
            int count = 0; // counter for step 1
            int count2 = 0; // counter for step 2
            
            for(int i = 0; i < utf8Text.length; i++) {
                int binText[] = {0,0,0,0,0,0,0,0};
                int binKey1_8b[] = {0,0,0,0,0,0,0,0};
                int binKey1_4b[] = {0,0,0,0};
                int binKey2_8b[] = {0,0,0,0,0,0,0,0};
                int binKey2_4b[] = {0,0,0,0};
                int cipher1[] = {0,0,0,0};
                int cipher2[] = {0,0,0,0};
                int cipher3[] = {0,0,0,0};
                int cipher4[] = {0,0,0,0};
                int decText = utf8Text[i];
                int decKey1 = 0;
                int decKey2 = 0;
                
                if (count < utf8Key1.length) {
                    decKey1 = utf8Key1[count];
                    count++;
                } else {
                    count = 0;
                }
                
                if (count2 < utf8Key2.length) {
                    decKey2 = utf8Key2[count2];
                    count2++;
                } else {
                    count2 = 0;
                }
                
                for (int j = 7; decText != 0; j--) {
                    binText[j] = decText%2;
                    decText = decText/2;
                }

                for (int j = 7; decKey1 != 0; j--) {
                    binKey1_8b[j] = decKey1%2;
                    decKey1 = decKey1/2;
                }
                
                for (int j = 7; decKey2 != 0; j--) {
                    binKey2_8b[j] = decKey2%2;
                    decKey2 = decKey2/2;
                }
                                
                // XOR 1
                for (int j = 0; j < 4; j++) {
                    binKey1_4b[j] = binKey1_8b[j]^binKey1_8b[j+4];
                }              
                        
                // XOR 2
                for (int j = 0; j < 4; j++) {
                    cipher1[j] = binText[j+4]^binKey1_4b[j];
                }
                
                // XOR 3
                for (int j = 0; j < 4; j++) {
                    cipher2[j] = binText[j]^cipher1[j];
                }
                                                
                // XOR 4
                for (int j = 0; j < 4; j++) {
                    binKey2_4b[j] = binKey2_8b[j]^binKey2_8b[j+4];
                }  
                
                // XOR 5
                for (int j = 0; j < 4; j++) {
                    cipher3[j] = cipher2[j]^binKey2_4b[j];
                }
                
                // XOR 6
                for (int j = 0; j < 4; j++) {
                    cipher4[j] = binText[j+4]^cipher3[j];
                }

                // Handle output
                String out = "";

                for(int j = 0; j < 4; j++) {
                    out = out + cipher2[j];
                }
                
                for(int j = 0; j < 4; j++) {
                    out = out + cipher4[j];
                }
                
                int decimalOutBits = Integer.parseInt(out,2);
                
                cipherOutput = cipherOutput + new String(BigInteger.valueOf(decimalOutBits).toByteArray(),"UTF8");
              
                byte[] byteOutBits = out.getBytes();
                listaBytesCifrados.add(byteOutBits);
           
            }            
        }   catch (UnsupportedEncodingException ex) {
        Logger.getLogger(FeistelCypher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Results(listaBytesCifrados, cipherOutput);
    }
        
    public static String decrypt(List<byte[]> input, String key1, String key2) {
        String decryptedOutput = "";
        try {
            byte[] utf8Key1 = key1.getBytes("UTF8");
            byte[] utf8Key2 = key2.getBytes("UTF8");            
            int count = 0; // counter for step 1
            int count2 = 0; // counter for step 2

            for (int i = 0; i < input.size(); i++) {
                int bin_txt[] = {0,0,0,0,0,0,0,0};
                int decCipherText;
                for (int j = 0; j < 8; j++) {
                    decCipherText = input.get(i)[j];
                    if (decCipherText == 48) {
                        bin_txt[j] = 0;
                    } else if (decCipherText == 49) {
                        bin_txt[j] = 1;
                    }
                }
               
                int decKey1 = 0;
                int decKey2 = 0;
                int binKey1_8b[] = {0,0,0,0,0,0,0,0};
                int binKey1_4b[] = {0,0,0,0};
                int binKey2_8b[] = {0,0,0,0,0,0,0,0};
                int binKey2_4b[] = {0,0,0,0};
                int cipher1[] = {0,0,0,0};
                int cipher2[] = {0,0,0,0};
                int cipher3[] = {0,0,0,0};
                int cipher4[] = {0,0,0,0};

                if (count < utf8Key1.length) {
                    decKey1 = utf8Key1[count];
                    count++;
                } else {
                    count = 0;
                }

                if (count2 < utf8Key2.length) {
                    decKey2 = utf8Key2[count2];
                    count2++;
                } else {
                    count2 = 0;
                }

                for (int j = 7; decKey1 != 0; j--) {
                    binKey1_8b[j] = decKey1%2;
                    decKey1 = decKey1/2;
                }
                
                for (int j = 7; decKey2 != 0; j--) {
                    binKey2_8b[j] = decKey2%2;
                    decKey2 = decKey2/2;
                }
                
                // XOR 1
                for (int j = 0; j < 4; j++) {
                    binKey2_4b[j] = binKey2_8b[j]^binKey2_8b[j+4];
                }    
                
                // XOR 2
                for (int j = 0; j < 4; j++) {
                    cipher1[j] = bin_txt[j]^binKey2_4b[j];
                }

                // XOR 3
                for (int j = 0; j < 4; j++) {
                    cipher2[j] = bin_txt[j+4]^cipher1[j];
                }

                // XOR 4
                for (int j = 0; j < 4; j++) {
                    binKey1_4b[j] = binKey1_8b[j]^binKey1_8b[j+4];
                }
                
                // XOR 5
                for (int j = 0; j < 4; j++) {
                    cipher3[j] = cipher2[j]^binKey1_4b[j];
                }

                // XOR 6
                for (int j = 0; j < 4; j++) {
                    cipher4[j] = bin_txt[j]^cipher3[j];
                }

                // Handle output
                String out = "";
                
                for(int j = 0; j < 4; j++) {
                    out = out + cipher4[j];
                }
                
                for(int j = 0; j < 4; j++) {
                    out = out + cipher2[j];
                }
                int dec_out = Integer.parseInt(out,2);               

                decryptedOutput = decryptedOutput + new String(BigInteger.valueOf(dec_out).toByteArray(),"UTF8");
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FeistelCypher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decryptedOutput;    
    }
    
    final static class Results {
        private final List<byte[]> listByteEncrypted;
        private final String textEncrypted;
        
        public Results(List<byte[]> listByteEncrypted, String textEncrypted) {
            this.listByteEncrypted = listByteEncrypted;
            this.textEncrypted = textEncrypted;
        }
        
        public List<byte[]> getListByteEncrypted() {
            return listByteEncrypted;
        }
        
        public String getTextEncrypted() {
            return textEncrypted;
        }
    }
}
