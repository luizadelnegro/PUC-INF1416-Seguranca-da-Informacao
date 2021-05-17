package controllers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.security.auth.x500.X500Principal;

public class X509CertificateHandler {
    
    X509Certificate cert;
    public X509CertificateHandler(FileInputStream fileInputStream) throws CertificateException, IOException {
        CertificateFactory certFact = CertificateFactory.getInstance("X.509");
        this.cert = (X509Certificate) certFact.generateCertificate(fileInputStream);
    }

    public X509CertificateHandler(String b64PublicKey) throws CertificateException {
        byte[] decodedPublicKey = Base64.getDecoder().decode(b64PublicKey);
        CertificateFactory certFact = CertificateFactory.getInstance("X.509");
        this.cert = (X509Certificate) certFact.generateCertificate(new ByteArrayInputStream(decodedPublicKey));
    }

    public String getEmail() {
        String principal = cert.getSubjectX500Principal().toString();
        return principal.split(",")[0].replace("EMAILADDRESS=", "").replace(" ", "");
    }

    public String getName() {
        String principal = cert.getSubjectX500Principal().toString();
        return principal.split(",")[1].replace("CN=", "").replace(" ", "");
    }

    public PublicKey getPublicKey() {
        return this.cert.getPublicKey();
    }

    public byte[] getEncoded() {
        try {
            return this.cert.getEncoded();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getConfirmationString() {
        return String.format("Versão: %d\nSérie: %s\nValidade: %s\nTipo de assinatura:%s\nEmissor: %s\nSujeito: %s\nEmail:%s\n", cert.getVersion(), cert.getSerialNumber().toString(), cert.getNotAfter().toString(), cert.getSigAlgName(), cert.getIssuerDN().toString(), getName(), getEmail());
    }
}
