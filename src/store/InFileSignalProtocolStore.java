package store;
import java.util.List;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

public class InFileSignalProtocolStore implements SignalProtocolStore {
	
	private final InFilePreKeyStore preKeyStore = new InFilePreKeyStore();
	private final InFileSessionStore sessionStore = new InFileSessionStore();
	private final InFileSignedPreKeyStore signedPreKeyStore = new InFileSignedPreKeyStore();
	private InFileIdentityKeyStore identityKeyStore = null;

	
	public InFileSignalProtocolStore(IdentityKeyPair identityKeyPair, int registrationId) {
		this.identityKeyStore = new InFileIdentityKeyStore(identityKeyPair, registrationId);
	}

	  @Override
	  public IdentityKeyPair getIdentityKeyPair() {
	    return identityKeyStore.getIdentityKeyPair();
	  }

	  @Override
	  public int getLocalRegistrationId() {
	    return identityKeyStore.getLocalRegistrationId();
	  }

	  @Override
	  public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
	    return identityKeyStore.saveIdentity(address, identityKey);
	  }

	  @Override
	  public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
	    return identityKeyStore.isTrustedIdentity(address, identityKey, direction);
	  }

	  @Override
	  public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
	    return preKeyStore.loadPreKey(preKeyId);
	  }

	  @Override
	  public void storePreKey(int preKeyId, PreKeyRecord record) {
	    preKeyStore.storePreKey(preKeyId, record);
	  }

	  @Override
	  public boolean containsPreKey(int preKeyId) {
	    return preKeyStore.containsPreKey(preKeyId);
	  }

	  @Override
	  public void removePreKey(int preKeyId) {
	    preKeyStore.removePreKey(preKeyId);
	  }

	  @Override
	  public SessionRecord loadSession(SignalProtocolAddress address) {
	    return sessionStore.loadSession(address);
	  }

	  @Override
	  public List<Integer> getSubDeviceSessions(String name) {
	    return sessionStore.getSubDeviceSessions(name);
	  }

	  @Override
	  public void storeSession(SignalProtocolAddress address, SessionRecord record) {
	    sessionStore.storeSession(address, record);
	  }

	  @Override
	  public boolean containsSession(SignalProtocolAddress address) {
	    return sessionStore.containsSession(address);
	  }

	  @Override
	  public void deleteSession(SignalProtocolAddress address) {
	    sessionStore.deleteSession(address);
	  }

	  @Override
	  public void deleteAllSessions(String name) {
	    sessionStore.deleteAllSessions(name);
	  }

	  @Override
	  public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
	    return signedPreKeyStore.loadSignedPreKey(signedPreKeyId);
	  }

	  @Override
	  public List<SignedPreKeyRecord> loadSignedPreKeys() {
	    return signedPreKeyStore.loadSignedPreKeys();
	  }

	  @Override
	  public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
	    signedPreKeyStore.storeSignedPreKey(signedPreKeyId, record);
	  }

	  @Override
	  public boolean containsSignedPreKey(int signedPreKeyId) {
	    return signedPreKeyStore.containsSignedPreKey(signedPreKeyId);
	  }

	  @Override
	  public void removeSignedPreKey(int signedPreKeyId) {
	    signedPreKeyStore.removeSignedPreKey(signedPreKeyId);
	  }

}
