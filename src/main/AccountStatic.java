package main;

import java.util.concurrent.TimeUnit;

import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.internal.push.SignalServiceUrl;

import store.TrustStoreFile;

public class AccountStatic {
	final static TrustStore TRUST_STORE = new TrustStoreFile();
	final static SignalServiceUrl[] URL = {
			new SignalServiceUrl("https://textsecure-service-staging.whispersystems.org", TRUST_STORE) };
	
	final static long timeout = 2;
	final static TimeUnit timeoutTimeUnit = TimeUnit.SECONDS;
	final static String USER_AGENT = "SIGNAL_JAVA";
}
