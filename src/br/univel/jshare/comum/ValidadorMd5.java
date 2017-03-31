package br.univel.jshare.comum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidadorMd5 {

	
	private static byte[] md5;

	public static String getMd5(byte[] bytes) {
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md5 = md.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return md5.toString();
	}
	
}
