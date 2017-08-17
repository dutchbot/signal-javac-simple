package main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.api.push.exceptions.AuthorizationFailedException;
import org.whispersystems.signalservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import org.whispersystems.signalservice.internal.push.SignalServiceUrl;

import serializer.AccountInfoSerializer;
import store.DbHolder;
import store.TrustStoreFile;

public class Register {

	private AccountInfo account = null;

	public void registerAccount(Scanner reader) {
		AccountInfoSerializer serializer = new AccountInfoSerializer();
		ConcurrentMap<String, AccountInfo> store = (ConcurrentMap<String, AccountInfo>) DbHolder.getDB()
				.hashMap("account").valueSerializer(serializer).createOrOpen();
		if (store.containsKey("acc")) {
			account = store.get("acc");
		} else {
			account = new AccountInfo();
			account.identityKey = KeyHelper.generateIdentityKeyPair();
			account.USERNAME = Config.getPhoneNumber();
			account.PASSWORD = Helper.generateRandomPassword();
			List<PreKeyRecord> oneTimePreKeys = KeyHelper.generatePreKeys(0, 100);

			// PreKeyRecord lastResortKey = KeyHelper.generateLastResortPreKey();
			int signedPreKeyId = 1;
			SignedPreKeyRecord signedPreKeyRecord = null;
			try {
				signedPreKeyRecord = KeyHelper.generateSignedPreKey(account.identityKey, signedPreKeyId);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}

			SignalServiceAccountManager accountManager = new SignalServiceAccountManager(AccountStatic.URL,
					account.USERNAME, account.PASSWORD, AccountStatic.USER_AGENT);
			try {
				accountManager.requestSmsVerificationCode();
			} catch (NonSuccessfulResponseCodeException e) {
				System.out.println("check if phone number is correct.");
			} catch (IOException e) {
				e.printStackTrace();
			}

			String verificationCode = getVerificationCode(reader);

			account.registrationId = KeyHelper.generateRegistrationId(false);
			account.signalingKey = Helper.generateRandomSignalingKey();
			boolean verificationSucces = false;
			try {
				accountManager.verifyAccountWithCode(verificationCode, account.signalingKey, account.registrationId,
						true);//fetches messages True means no GCM needed :D
				verificationSucces = true;
				// required for signal-desktop/android/ios?
				//Optional<String> gcmReg = Optional.of(Integer.toString(account.registrationId));
				//accountManager.setGcmId(gcmReg);
			} catch (AuthorizationFailedException e) {
				System.out.println("Verification code was wrong"); // See wiki of signal-server on github
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (verificationSucces) {
					accountManager.setPreKeys(account.identityKey.getPublicKey(), signedPreKeyRecord, oneTimePreKeys);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			store.put("acc", account);
			System.out.println("All good !");
		}
	}

	private String getVerificationCode(Scanner reader) {
		try {
			System.out.println("Give the SMS code for verification: ");
			String verificationCode = fixVerificationFormat(reader.nextLine());
			return verificationCode;
		} catch (Exception e) {
			System.out.println("Error: Verification code must be 6 or 7 chars long.");
			return getVerificationCode(reader);
		}
	}

	public static String fixVerificationFormat(String verificationCode) throws Exception {
		if (verificationCode.length() < 7) {
			throw new Exception("Invalid verificationCode!");
		}
		if (verificationCode.contains("-")) {
			String[] splitted = verificationCode.split("-");
			verificationCode = splitted[0] + splitted[1];
		}
		return verificationCode;
	}

	public AccountInfo getAccountInfo() {
		return account;
	}

}
