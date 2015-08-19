package cn.brent.toolbox.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptUtil {

	public static final int DRUPAL_HASH_LENGTH = 55;
	public static final int DRUPAL_MIN_HASH_COUNT = 7;
	public static final int DRUPAL_MAX_HASH_COUNT = 30;
	public static final int DRUPAL_HASH_COUNT = 15;

	public static final String B64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String randomSetting(int countLog2) {
		if (countLog2 < DRUPAL_MIN_HASH_COUNT)
			countLog2 = DRUPAL_HASH_COUNT;
		if (countLog2 > DRUPAL_MAX_HASH_COUNT)
			countLog2 = DRUPAL_MAX_HASH_COUNT;
		String setting = "$S$" + B64.charAt(countLog2);
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 8; i++) {
			setting += B64.charAt(random.nextInt(B64.length()));
		}
		return setting;
	}

	private static String randomSetting() {
		return randomSetting(0);
	}

	public static boolean checkPassword(String candidate, String saltedEncryptedPassword) {
		if (candidate == null || saltedEncryptedPassword == null) {
			return false;
		}
		String hash;
		try {
			hash = encrpyt(candidate, saltedEncryptedPassword);
		} catch (Exception e) {
			return false;
		}
		return saltedEncryptedPassword.equalsIgnoreCase(hash);
	}

	public static String generatePassword(String password) {
		try {
			return encrpyt(password, randomSetting());
		} catch (Exception e) {
			return "";
		}
	}

	private static String encrpyt(String password, String setting) throws Exception {
		// The first 12 characters of an existing hash are its setting string.
		setting = setting.substring(0, 12);
		int count = parseCount(setting);
		String salt = setting.substring(4, 12);
		// Hashes must have an 8 character salt.
		if (salt.length() != 8) {
			return null;
		}

		byte[] pwdBytes = password.getBytes();
		byte[] saltBytes = salt.getBytes();

		byte[] hash = sha512(joinBytes(saltBytes, pwdBytes));
		for (int i = 0; i < count; i++) {
			hash = sha512(joinBytes(hash, pwdBytes));
		}

		String output = setting + base64Encode(hash, hash.length);
		return (output.length() > 0) ? output.substring(0, DRUPAL_HASH_LENGTH) : null;
	}

	private static byte[] joinBytes(byte[] a, byte[] b) {
		byte[] combined = new byte[a.length + b.length];

		System.arraycopy(a, 0, combined, 0, a.length);
		System.arraycopy(b, 0, combined, a.length, b.length);
		return combined;
	}

	private static String base64Encode(byte[] input, int count) throws Exception {

		StringBuffer output = new StringBuffer();
		int i = 0;
		do {
			long value = input[i++] & 0xFF;

			output.append(B64.charAt((int) value & 0x3f));
			if (i < count) {
				value |= (input[i] & 0xFF) << 8;
			}
			output.append(B64.charAt((int) (value >> 6) & 0x3f));
			if (i++ >= count) {
				break;
			}
			if (i < count) {
				value |= (input[i] & 0xFF) << 16;
			}

			output.append(B64.charAt((int) (value >> 12) & 0x3f));
			if (i++ >= count) {
				break;
			}
			output.append(B64.charAt((int) (value >> 18) & 0x3f));
		} while (i < count);

		return output.toString();
	}

	private static int parseCount(String setting) {
		int countLog2 = B64.indexOf(setting.charAt(3));
		if (countLog2 < DRUPAL_MIN_HASH_COUNT)
			return DRUPAL_MIN_HASH_COUNT;
		if (countLog2 > DRUPAL_MAX_HASH_COUNT)
			return DRUPAL_MAX_HASH_COUNT;

		return 1 << countLog2;
	}

	private static byte[] sha512(byte[] input) {
		try {
			MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
			return md.digest(input);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return new byte[0];
	}
}
