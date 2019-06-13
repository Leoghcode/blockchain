package com.example.blockchain.service;

import sun.security.ec.ECPrivateKeyImpl;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KeyUtil {
    public static String getSHA256Str(String text) {
        String encodeText = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes("UTF-8"));
            encodeText = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeText;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String tmp;
        for (byte aByte : bytes) {
            tmp = Integer.toHexString(aByte & 0xFF);
            if (tmp.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(tmp);
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> getSHAKeys() {
        Map<String, String> hashMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
            keyPairGenerator.initialize(256);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

            String publicKeyHex = bytes2Hex(ecPublicKey.getEncoded());
            Base64.Encoder encoder = Base64.getEncoder();
            String publicKeyBase64 = encoder.encodeToString(hex2Bytes(publicKeyHex));
            String privateKey = bytes2Hex(ecPrivateKey.getEncoded());
            hashMap.put("public_key", publicKeyBase64);
            hashMap.put("private_key", privateKey);
//            System.out.println(hashMap);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static Map<String, String> signMessage(String privateKey) {
        Map<String, String> hashMap = new HashMap<>();
        String message = String.valueOf(new Date().getTime());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(hex2Bytes(privateKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey1);
            signature.update(message.getBytes());


            Base64.Encoder encoder = Base64.getEncoder();
            String sign = encoder.encodeToString(signature.sign());
            hashMap.put("message", message);
            hashMap.put("signature", sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static boolean verify(String publicKey, String message, String signature) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] sign = decoder.decode(signature);
        byte[] publicKeyBytes = decoder.decode(publicKey);

        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey1 = keyFactory.generatePublic(keySpec);
            Signature signature1 = Signature.getInstance("SHA256withECDSA");
            signature1.initVerify(publicKey1);
            signature1.update(message.getBytes());
            return signature1.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        System.out.println("no signature");
        return false;
    }
    private static String bytes2Hex(byte[] bytes) {
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int length = bytes.length;
        char[] trans = new char[length << 1];
        for(int i = 0, j = 0; i < length; i++) {
            trans[j++] = digits[(0xF0 & bytes[i]) >>> 4];
            trans[j++] = digits[(0x0F & bytes[i])];
        }
        return String.valueOf(trans);
    }
    private static byte[] hex2Bytes(String hex) {
        hex = hex.length() % 2 != 0 ? "0" + hex : hex;
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int integer = Integer.parseInt(hex.substring(index, index+2), 16);
            bytes[i] = (byte)integer;
        }
        return bytes;
    }
    public static void main(String[] args) {
        Map<String, String> hashMap = getSHAKeys();
        System.out.println(hashMap.get("address"));
        System.out.println(hashMap.get("private_key"));
        Map<String, String> hashMap2 = signMessage(hashMap.get("private_key"));
        System.out.println(hashMap2.get("message"));
        System.out.println(hashMap2.get("signature"));

        System.out.println(verify(hashMap.get("address"), hashMap2.get("message"), hashMap2.get("signature")));
    }
}
