package hk.org.ha.eclaim.core.helper;

import java.util.Base64;

public class CryptoHelper {

	public static String encode(String str) throws Exception {
		try {
			String encodedString = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
			return encodedString;
		} catch (Exception e) {
			throw new Exception("Fail to encode");
		}
	}

	public static String decode(String str) throws Exception {
		try {
			byte[] decodedByte = Base64.getDecoder().decode(str);
			String decodedString = new String(decodedByte);
			return decodedString;
		} catch (Exception e) {
			throw new Exception("Fail to decode");
		}
	}
	
}
