package gui;

import java.util.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String CIPHER_ALGORITHM = "RSA/None/PKCS1Padding";
	static {
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	static Base64.Encoder encoder = Base64.getEncoder();
	static Base64.Decoder decoder = Base64.getDecoder();
	private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //���ڷ�װ��������Ĺ�Կ��˽Կ
	
	/*
	/**
	 * ���ع�Կ
	 * @param name ͨ�Ŷ����� Server/Client
	 * @return ��Կ
	 * @throws Exception
	 *
	public static String getPubKey(String name){
		try {
			genKeyPair(name);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return keyMap.get(0);
	}
	/**
	 * ����˽Կ
	 * @param name ͨ�Ŷ����� Server/Client
	 * @return ˽Կ
	 * @throws Exception
	 *
	public static String getPriKey(String name){
		try {
			genKeyPair(name);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return keyMap.get(1);
	}*/
	
	static Map<Integer, String> ServerkeyMap = genKeyPair("Server");
	static Map<Integer, String> ClientkeyMap = genKeyPair("Client");
	
	/** 
	 * ���������Կ�� 
	 * @param name ��Կ������
	 * @return keyMap�������Կ��
	 * @throws NoSuchAlgorithmException 
	 */  
	public static Map<Integer, String> genKeyPair(String name) {  
		//KeyPairGenerator���������ɹ�Կ��˽Կ�ԣ�����RSA�㷨���ɶ���  
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			//��ʼ����Կ������������Կ��СΪ96-1024λ 
			keyPairGen.initialize(1024,new SecureRandom(name.getBytes())); 
			//����һ����Կ�ԣ�������keyPair��  
			KeyPair keyPair = keyPairGen.generateKeyPair();  
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   //�õ�˽Կ  
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  //�õ���Կ  
			String publicKeyString = new String(encoder.encodeToString(publicKey.getEncoded()));  
			//�õ�˽Կ�ַ���  
			String privateKeyString = new String(encoder.encodeToString((privateKey.getEncoded())));  
			//����Կ��˽Կ���浽Map
			keyMap.put(0,publicKeyString);  //0��ʾ��Կ
			keyMap.put(1,privateKeyString);  //1��ʾ˽Կ
		} catch (NoSuchAlgorithmException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 
		return keyMap;
	}
	
	/** 
	 * RSA��Կ����   
	 * @param str ��Ҫ���ܵ��ַ���
	 * @param publicKey ��Կ 
	 * @return ���� 
	 * @throws Exception
	 */  
	public static String encrypt(String str, String publicKey) throws Exception{
		//base64����Ĺ�Կ
		byte[] decoded = decoder.decode(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
		//RSA����
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = encoder.encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/** 
	 * RSA˽Կ����
	 * @param str ��Ҫ���ܵ��ַ���
	 * @param privateKey ˽Կ 
	 * @return ����
	 * @throws Exception
	 */  
	public static String decrypt(String str, String privateKey) throws Exception{
		//64λ������ܺ���ַ���
		byte[] inputByte = decoder.decode(str.getBytes("UTF-8"));
		//base64�����˽Կ
		byte[] decoded = decoder.decode(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA����
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	
	/** 
	 * ǩ��
	 * @param str ��Ҫǩ��������
	 * @param privateKey ǩ���ߵ�˽Կ
	 * @return ǩ��
	 * @throws Exception
	 */
	public static byte[] sign(String str, String privateKey) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
	        signature.initSign(String2PriKey(privateKey));
	        signature.update(str.getBytes());
	        byte[] result = signature.sign();
			return result;
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
		}
		return null;
	}
	
	/** 
	 * ��֤
	 * @param str ���ܺ������
	 * @param publicKey ��֤�ߵĹ�Կ
	 * @param result ǩ��
	 * @throws Exception
	 */
	public static void verify(String str, String publicKey, byte[] result) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
	        signature.initVerify(String2PubKey(publicKey));
	        signature.update(str.getBytes());
			boolean bool = signature.verify(result);
			System.out.println("ǩ����֤�����" + bool + "\n");
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
		}
	}
	
	public static PublicKey String2PubKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = decoder.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
	
	public static PrivateKey String2PriKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = decoder.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
	
	/*
	public static void main(String[] args) throws Exception {
		//���ɹ�Կ��˽Կ
		String name = "Server";
		//String name = "Client";
		genKeyPair(name);
		//�����ַ���
		String message = "1787085EE71C3865";
		System.out.println("������ɵĹ�ԿΪ:" + keyMap.get(0));
		System.out.println("������ɵ�˽ԿΪ:" + keyMap.get(1));
		String messageEn = encrypt(message,keyMap.get(0));
		System.out.println(message + "���ܺ���ַ���Ϊ:" + messageEn);
		String messageDe = decrypt(messageEn,keyMap.get(1));
		System.out.println("��ԭ����ַ���Ϊ:" + messageDe);
	}*/
	
}
