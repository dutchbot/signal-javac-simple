package serializer;

import java.io.IOException;
import java.io.Serializable;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import main.AccountInfo;

public class AccountInfoSerializer implements Serializer<AccountInfo>, Serializable {
	
	int keypairbuf_len = 69;

	@Override
	public AccountInfo deserialize(DataInput2 s, int arg1) throws IOException {
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.USERNAME = s.readUTF();
		accountInfo.PASSWORD = s.readUTF();
		accountInfo.registrationId = s.readInt();
		accountInfo.signalingKey = s.readUTF();
		byte[] keypair = new byte[keypairbuf_len]; 
		s.readFully(keypair);
		try {
			accountInfo.identityKey = new IdentityKeyPair(keypair);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountInfo;
	}

	@Override
	public void serialize(DataOutput2 out, AccountInfo acc) throws IOException {
		out.writeUTF(acc.USERNAME);
		out.writeUTF(acc.PASSWORD);
		out.writeInt(acc.registrationId);
		out.writeBytes(acc.signalingKey);
		byte[] keypair = acc.identityKey.serialize();
		keypairbuf_len = keypair.length;
		out.write(keypair);
	}
}
