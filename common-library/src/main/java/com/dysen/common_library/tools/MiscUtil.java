//////////////////////////////////////////////////////////////////////
//
// MiscUtil.java: implementation of the MiscUtil class.
//
///////////////////////////////////////////////////////////////////////////////

package com.dysen.common_library.tools;

import java.security.MessageDigest;

public class MiscUtil {
	public static String getMD5(byte[] bytes) {
		String strTemp = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest(), "");
		} catch (Exception e) {
			strTemp = "";
		}

		return strTemp;
	}

	public static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			if (Integer.toHexString(0xFF & b).length() == 1)
				hexString.append("0").append(Integer.toHexString(0xFF & b));
			else
				hexString.append(Integer.toHexString(0xFF & b));
		}
		return hexString.toString();
	}


	
}
