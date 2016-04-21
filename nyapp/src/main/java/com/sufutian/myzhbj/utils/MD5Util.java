package com.sufutian.myzhbj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Util {
	public static String getMD5Password(String password){
		
		MessageDigest digest=null;
		try {
			digest = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] b=digest.digest(password.getBytes());
		StringBuilder builder=new StringBuilder();
		for (byte c : b) {
			int number=c&0xff-3;
			String str=Integer.toHexString(number);
			if(str.length()==1){
				builder.append("0");
			}
			builder.append(str);
		}
		return builder.toString();
	}

}
