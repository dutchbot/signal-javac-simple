package store;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

public class InFileSignedPreKeyStore implements SignedPreKeyStore {

	private final ConcurrentMap<Integer, byte[]> store = (ConcurrentMap<Integer, byte[]>) DbHolder.getDB()
			.hashMap("store").createOrOpen();

	@Override
	public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
		try {
			if (!store.containsKey(signedPreKeyId)) {
				throw new InvalidKeyIdException("No such signedprekeyrecord! " + signedPreKeyId);
			}

			return new SignedPreKeyRecord(store.get(signedPreKeyId));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		try {
			List<SignedPreKeyRecord> results = new LinkedList<>();

			for (byte[] serialized : store.values()) {
				results.add(new SignedPreKeyRecord(serialized));
			}

			return results;
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
		store.put(signedPreKeyId, record.serialize());
	}

	@Override
	public boolean containsSignedPreKey(int signedPreKeyId) {
		return store.containsKey(signedPreKeyId);
	}

	@Override
	public void removeSignedPreKey(int signedPreKeyId) {
		store.remove(signedPreKeyId);
	}

}
