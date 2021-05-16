package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class X509CertificateHandler {
    
    X509Certificate cert;
    public X509CertificateHandler(String pathToCert) throws CertificateException, FileNotFoundException, IOException {
        CertificateFactory certFact = CertificateFactory.getInstance("X.509");
        FileInputStream is = new FileInputStream (pathToCert);
        this.cert = (X509Certificate) certFact.generateCertificate(is);
        is.close();
    }

    public PublicKey getPublicKey() {
        return this.cert.getPublicKey();
    }
}
