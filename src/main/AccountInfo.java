package main;
import java.io.Serializable;

import org.whispersystems.libsignal.IdentityKeyPair;

public class AccountInfo implements Serializable{
	
	public String USERNAME = null;
	public String PASSWORD = null;
	public int registrationId = -1;
	public String signalingKey = null;
	public IdentityKeyPair identityKey = null;
	
}
