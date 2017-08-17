package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.util.KeyHelper;

import junit.framework.Assert;
import main.AccountInfo;
import main.Config;
import main.Helper;
import main.Register;
import serializer.AccountInfoSerializer;
import serializer.IdentityKeySerializer;
import serializer.KeySignalAddressSerializer;
import store.DbHolder;

public class TryOut {
	
	@BeforeClass
	public static void setup() {
		DbHolder.initialize(true);
	}

	@Test
	public void testVerificationCode() {
		String verificationCode = "869-507"; // length 6
		try {
			System.out.println(Register.fixVerificationFormat(verificationCode));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Assert.assertFalse("Failed to remove '-'", Register.fixVerificationFormat(verificationCode).contains("-"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAccountInfo() {
		AccountInfoSerializer serializer = new AccountInfoSerializer();
		ConcurrentMap<String, AccountInfo> store = (ConcurrentMap<String, AccountInfo>) DbHolder.getDB()
				.hashMap("account").valueSerializer(serializer).createOrOpen();
		if (!store.containsKey("acc")) {
			AccountInfo account = new AccountInfo();
			account.identityKey = KeyHelper.generateIdentityKeyPair();
			account.USERNAME = Config.getPhoneNumber();
			account.PASSWORD = Helper.generateRandomPassword();
			account.registrationId = KeyHelper.generateRegistrationId(false);
			account.signalingKey = Helper.generateRandomSignalingKey();
			store.put("acc", account);
		}

		store.get("acc");
	}

	@Test
	public void testSerialize() {
		KeySignalAddressSerializer keySerializer = new KeySignalAddressSerializer();
		IdentityKeySerializer serializer = new IdentityKeySerializer();
		ConcurrentMap<SignalProtocolAddress, IdentityKey> trustedKeys = (ConcurrentMap<SignalProtocolAddress, IdentityKey>) DbHolder
				.getDB().hashMap("trustedKeys").keySerializer(keySerializer).valueSerializer(serializer).createOrOpen();
		IdentityKeyPair keyIdent = null;
		try {
			keyIdent = new IdentityKeyPair(KeyHelper.generateIdentityKeyPair().serialize());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SignalProtocolAddress address = new SignalProtocolAddress("+31622222222", 1);
		System.out.println(address.getClass());
		System.out.println(address.getName());
		System.out.println(address.getDeviceId());

		trustedKeys.put(address, keyIdent.getPublicKey());
	}

	@AfterClass
	public static  void tearDown() throws Exception {
		DbHolder.getDB().close();
	}
}
