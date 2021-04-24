package gui;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil {
	//�㷨����/����ģʽ/��䷽ʽ
    public static final String KEY_ALGORITHM = "DES";
    //DES�������ֹ���ģʽ ECB���������뱾ģʽ��CBC�����ܷ�������ģʽ��CFB�����ܷ���ģʽ��OFB���������ģʽ
    //�������ģʽ NoPadding��Zeros��PKCS5Padding
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
 
    /** 
     * ������Կkey����
     * @param KeyStr ��Կ�ַ���
     * @return ��Կ����
     * @throws Exception
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        //�����ܳ׹�������DESKeySpecת����SecretKey����
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }
 
    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
 
    //ʮ�������ַ���ת�����ֽ�����
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }
 
    /**
     * DES ECBģʽ ����
     * @param data ����
     * @param key ��Կ
     * @return Base64���������
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        //ʵ����Cipher�������������ʵ�ʵļ��ܲ���
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();
        //��ʼ��Cipher��������Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        //ִ�м��ܲ��������ܺ�Ľ��ͨ��Base64������д���
        Base64.Encoder encoder = Base64.getEncoder();
        //System.out.println("key:" + encoder.encodeToString(deskey.getEncoded()));
        return encoder.encodeToString(cipher.doFinal(data.getBytes()));
    }
 
    /**
     * DES ECBģʽ ����
     * @param data Base64���������
     * @param key ��Կ
     * @return ����
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //��ʼ��Cipher��������Ϊ����ģʽ
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        //ִ�н��ܲ��������ܺ�Ľ��ͨ��Base64���뻹ԭ
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(cipher.doFinal(decoder.decode(data)));
    }
    
    /*
    public static void main(String[] args) throws Exception {
        String data = "��� helloworld";
        String key = "A1B2C3D4E5F60708";
        String encryptData = encrypt(data, key);
        System.out.println("���ܺ�: " + encryptData);
        String decryptData = decrypt(encryptData, key);
        System.out.println("���ܺ�: " + decryptData);
    }*/
}