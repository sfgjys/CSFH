package com.minji.cufcs.uitls;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class DecoderUtil {
	public static String decoder(String str) {
		try {
			str = str != null && !str.equals("") ? URLDecoder.decode(str,
					"utf-8") : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String encoder(String str) {
		try {
			str = str != null && !str.equals("") ? URLEncoder.encode(str,
					"utf-8") : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
