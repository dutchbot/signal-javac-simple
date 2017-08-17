package main;

import java.io.IOException;
import java.security.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.state.impl.InMemorySignalProtocolStore;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessagePipe;
import org.whispersystems.signalservice.api.SignalServiceMessageReceiver;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.SignalServiceCipher;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceContent;
import org.whispersystems.signalservice.api.messages.SignalServiceDataMessage;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import store.DbHolder;
import store.InFileSignalProtocolStore;

public class Main {
	static boolean listeningForMessages = true;

	public static void main(String[] args) {
		Scanner messageReader = new Scanner(System.in); // Reading from System.in
		DbHolder.initialize(false);
		try {
			Register register = new Register();
			register.registerAccount(messageReader);

			AccountInfo account = register.getAccountInfo();

			SignalServiceMessageSender messageSender = new SignalServiceMessageSender(AccountStatic.URL,
					account.USERNAME, account.PASSWORD,
					new InFileSignalProtocolStore(account.identityKey, account.registrationId),
					AccountStatic.USER_AGENT, Optional.absent(), Optional.absent());// TODO ADD eventlistener

			String to = Config.getToPhoneNumber();

			// incorrect approach it seems.
			Runnable r = new Runnable() {
				public void run() {
					try {
						receiveMessage(account);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			Thread listenForMessages = new Thread(r);
			listenForMessages.start();

			while (messageReader.hasNextLine()) {
				System.out.println("Type a message: ");
				String body = messageReader.nextLine();
				if (body.equals("/exit()")) {
					listeningForMessages = false;
					break;
				}
				long timestamp = System.currentTimeMillis(); // this works like expected on win10
				SignalServiceDataMessage message = new SignalServiceDataMessage(timestamp, body);
				try {
					messageSender.sendMessage(new SignalServiceAddress(to), message);
				} catch (UntrustedIdentityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			messageReader.close();
		}
	}

	private static void receiveMessage(AccountInfo account) throws InterruptedException { // Background thread..
		SignalServiceMessageReceiver messageReceiver = new SignalServiceMessageReceiver(AccountStatic.URL,
				account.USERNAME, account.PASSWORD, account.signalingKey, AccountStatic.USER_AGENT);
		SignalServiceMessagePipe messagePipe = null;

		try {

			try {
				while (listeningForMessages) {
					// this acknowledges the message.
					List<SignalServiceEnvelope> l = messageReceiver.retrieveMessages();
					for (SignalServiceEnvelope env : l) {
						if (env.isReceipt()) {
							System.out.println("Other party read our last message");
						} else {
							SignalServiceCipher cipher = new SignalServiceCipher(
									new SignalServiceAddress(account.USERNAME),
									new InFileSignalProtocolStore(account.identityKey, account.registrationId));
							SignalServiceContent message = cipher.decrypt(env); // errors on type 5 RECEIPT from other
																				// party

							System.out.println("Received message: " + message.getDataMessage().get().getBody().get());
						}
					}
					Thread.sleep(AccountStatic.timeout);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (InvalidVersionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicateMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.whispersystems.libsignal.UntrustedIdentityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LegacyMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (messagePipe != null)
				messagePipe.shutdown();
		}
	}

}
