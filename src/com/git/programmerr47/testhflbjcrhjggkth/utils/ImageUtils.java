package com.git.programmerr47.testhflbjcrhjggkth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Color;
import android.util.Log;

public class ImageUtils {
	private static int MAX_8BIT_RED = 8;
	private static int MAX_8BIT_GREEN = 8;
	private static int MAX_8BIT_BLUE = 4;

	private static byte[] getMD5(String str) {
		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(str.getBytes());
	        byte messageDigest[] = digest.digest();
	        return messageDigest;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new byte[16];
	}
	
	private static int[] getPixel8bitColors(String str) {
		int[] result = new int[64];
		int index = 0;
		int parts = 4;
		
		for (int i = 0; i < parts; i++) {
			String strPart = str.substring(str.length() * i / parts, str.length() * (i + 1) / 4);
			byte[] answer = getMD5(strPart);
			for (int k = 0; (k < answer.length) && (index < result.length); k++) {
				result[index] = answer[k] + 128;
				index++;
			}
		}
		return result;
	}
	
	private static int conver8bitTo24bit(int color) {
		int r = color / (MAX_8BIT_GREEN * MAX_8BIT_BLUE);
		int g = (color % (MAX_8BIT_GREEN * MAX_8BIT_BLUE)) / MAX_8BIT_BLUE;
		int b = (color % (MAX_8BIT_GREEN * MAX_8BIT_BLUE)) % MAX_8BIT_BLUE;
		return Color.rgb(r * 255 / MAX_8BIT_RED, g * 255 / MAX_8BIT_GREEN, b * 255 / MAX_8BIT_BLUE);
	}
	
	public static int[] getColors(String str) {
		int[] result = getPixel8bitColors(str);
		for (int i = 0; i < result.length; i++) {
			result[i] = conver8bitTo24bit(result[i]);
		}
		return result;
	}
}
