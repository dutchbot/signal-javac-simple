package store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.signalservice.api.push.TrustStore;

public class TrustStoreFile implements TrustStore {

	private final String file_loc = "keystore.bks";
	private KeyStore holder = null;

	@Override
	public InputStream getKeyStoreInputStream() {
		try {
			if (holder == null) {
				Security.addProvider(new BouncyCastleProvider());
				Path path = Paths.get(file_loc);

				if (Files.exists(path)) {
					FileInputStream file = null;
					file = new FileInputStream(file_loc);
					return file;
				} else {
					KeyStore key = KeyStore.getInstance("BKS");
					CertificateFactory fact = CertificateFactory.getInstance("X.509");
					FileInputStream is = new FileInputStream("signal-staging.cer");
					X509Certificate cer = (X509Certificate) fact.generateCertificate(is);

					String pass = getKeyStorePassword();
					char[] pwd = pass.toCharArray();
					key.load(null, pwd);
					key.setCertificateEntry("signal_ssl", cer);
					FileOutputStream output = new FileOutputStream(file_loc);
					key.store(output, pwd);
					output.close();
					holder = key;
				}
			}

			FileInputStream file = null;
			file = new FileInputStream(file_loc);
			return file;
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// sSystem.out.print(e.getMessage());
		}
		return null;
	}

	@Override
	public String getKeyStorePassword() {
		return "abc@123";
	}

}
