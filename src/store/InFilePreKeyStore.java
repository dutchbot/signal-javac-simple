package store;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;

public class InFilePreKeyStore implements PreKeyStore {

	private final ConcurrentMap<Integer, byte[]> store = (ConcurrentMap<Integer, byte[]>) DbHolder.getDB().hashMap("store").createOrOpen();
	
	@Override
	public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
		try {
			if (!store.containsKey(preKeyId)) {
				throw new InvalidKeyIdException("No such prekeyrecord!");
			}

			return new PreKeyRecord(store.get(preKeyId));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public void storePreKey(int preKeyId, PreKeyRecord record) {
		store.put(preKeyId, record.serialize());
	}

	@Override
	public boolean containsPreKey(int preKeyId) {
		return store.containsKey(preKeyId);
	}

	@Override
	public void removePreKey(int preKeyId) {
		store.remove(preKeyId);
	}
}
