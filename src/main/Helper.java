package main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.whispersystems.libsignal.state.PreKeyRecord;

public class Helper {

	public static String generateRandomSignalingKey() {
		byte[] bytes = new byte[52];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes);
			return new String(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String generateRandomPassword() {
		byte[] bytes = new byte[16];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes);
			return new String(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PreKeyRecord generateLastResortPreKey() {
		byte[] lastPreKeyByte = { 0xC };
		try {
			return new PreKeyRecord(lastPreKeyByte);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
