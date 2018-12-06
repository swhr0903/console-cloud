package com.cloud.console.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Frank on 2017/8/7.
 *
 * RSA工具类
 */
public class RSAUtils {
    public static KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());

        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    public static void saveKey(KeyPair keyPair, String keyFile) throws ConfigurationException, IOException {
        File file = new File(keyFile);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        PublicKey pubkey = keyPair.getPublic();
        PrivateKey prikey = keyPair.getPrivate();
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(keyFile);
        propertiesConfiguration.setProperty("puliic_key", toHexString(pubkey.getEncoded()));
        propertiesConfiguration.setProperty("private_key", toHexString(prikey.getEncoded()));
        propertiesConfiguration.save();
    }

    /**
     * @param filename
     * @param type：    1-public 0-private
     * @return
     * @throws ConfigurationException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static Key loadKey(String filename, int type)
            throws ConfigurationException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        PropertiesConfiguration config = new PropertiesConfiguration(filename);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA",
                new BouncyCastleProvider());

        if (type == 0) {
            String privateKeyValue = config.getString("private_key");
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    toBytes(privateKeyValue));
            PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
            return privateKey;

        } else {
            String publicKeyValue = config.getString("puliic_key");
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                    toBytes(publicKeyValue));
            PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);
            return publicKey;
        }
    }

    /**
     * Encrypt String.
     *
     * @return byte[]
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] data) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA",
                        new BouncyCastleProvider());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Basic decrypt method
     *
     * @return byte[]
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] raw) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA",
                        new BouncyCastleProvider());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static final byte[] toBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }
        return bytes;
    }

    private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static void main(String[] args) {
        try {
            String encryptText = "frank";
            // Generate keys
            KeyPair keyPair = generateKey();
            //RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            //RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            String keyfile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "keys/frank.txt";
            saveKey(keyPair, keyfile);
            Key privateKey = loadKey(keyfile, 0);
            Key publicKey = loadKey(keyfile, 1);
            byte[] e = encrypt((RSAPublicKey) publicKey, encryptText.getBytes());
            byte[] de = decrypt((RSAPrivateKey) privateKey, e);
            System.out.println(toHexString(e));
            System.out.println(new String(de));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
