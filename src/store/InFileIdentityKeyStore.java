package store;

import java.util.concurrent.ConcurrentMap;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import serializer.KeySignalAddressSerializer;
import serializer.IdentityKeySerializer;

public class InFileIdentityKeyStore implements IdentityKeyStore {

	private final KeySignalAddressSerializer keySerializer = new KeySignalAddressSerializer();
	private final IdentityKeySerializer serializer = new IdentityKeySerializer();
	private final ConcurrentMap<SignalProtocolAddress, IdentityKey> trustedKeys = 
			(ConcurrentMap<SignalProtocolAddress, IdentityKey>) DbHolder.getDB().hashMap("trustedKeys").keySerializer(keySerializer).valueSerializer(serializer).createOrOpen();
	private final IdentityKeyPair identityKeyPair;
	private final int localRegistrationId;

	public InFileIdentityKeyStore(IdentityKeyPair identityKeyPair, int localRegistrationId) {
		this.identityKeyPair = identityKeyPair;
		this.localRegistrationId = localRegistrationId;
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return identityKeyPair;
	}

	@Override
	public int getLocalRegistrationId() {
		return localRegistrationId;
	}

	@Override
	public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
		IdentityKey existing = trustedKeys.get(address);

		if (!identityKey.equals(existing)) {
			trustedKeys.put(address, identityKey);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
		IdentityKey trusted = trustedKeys.get(address);
		return (trusted == null || trusted.equals(identityKey));
	}

}
