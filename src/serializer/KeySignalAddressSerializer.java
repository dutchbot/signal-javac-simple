package serializer;

import java.io.IOException;
import java.io.Serializable;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.whispersystems.libsignal.SignalProtocolAddress;

public class KeySignalAddressSerializer implements Serializer<SignalProtocolAddress>, Serializable {

	@Override
	public SignalProtocolAddress deserialize(DataInput2 in, int arg1) throws IOException {
		return new SignalProtocolAddress(in.readUTF(),in.readInt());
	}

	@Override
	public void serialize(DataOutput2 out, SignalProtocolAddress spa) throws IOException {
		out.writeUTF(spa.getName());
		out.writeInt(spa.getDeviceId());
	}
}
