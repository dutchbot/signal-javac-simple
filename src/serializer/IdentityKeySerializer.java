package serializer;

import java.io.IOException;
import java.io.Serializable;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;

public class IdentityKeySerializer implements Serializer<IdentityKey>, Serializable {

	@Override
	public IdentityKey deserialize(DataInput2 arg0, int arg1) throws IOException {
		byte[] bytes = new byte[33]; // only pub..
		arg0.readFully(bytes);
		try {
			return new IdentityKey(bytes, 0);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void serialize(DataOutput2 arg0, IdentityKey arg1) throws IOException {
		// TODO Auto-generated method stub
		byte[] bytes = arg1.serialize();
		arg0.write(bytes);
		
	}
}
