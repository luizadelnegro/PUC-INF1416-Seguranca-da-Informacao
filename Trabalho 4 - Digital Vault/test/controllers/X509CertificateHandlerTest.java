package test.controllers;

import controllers.X509CertificateHandler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import test.controllers.PrivateKeyHandlerTest;
import org.junit.jupiter.api.Test;


public class X509CertificateHandlerTest {

    static PublicKey getAdminPublicKey() {
        X509CertificateHandler adminCert = null;
        PublicKey pubK = null;
        try {
            FileInputStream is = new FileInputStream ("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/admin-x509.crt");
            adminCert = new X509CertificateHandler(is);
            is.close();
            pubK = adminCert.getPublicKey();
            System.out.println(((RSAPublicKey) pubK).getModulus().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubK;
    }
    public static PublicKey getUserPublicKey() {
        X509CertificateHandler userCert = null;
        PublicKey pubK = null;
        try {
            FileInputStream is = new FileInputStream ("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/user01-x509.crt");
            userCert = new X509CertificateHandler(is);
            is.close();
            pubK = userCert.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubK;
    }

    @Test
    void testGetAdminPublicKeyFromCert() {
        RSAPublicKey pubKey = (RSAPublicKey) getAdminPublicKey();
        assertNotEquals(null, pubKey);
        String expectedModulus = "24694716545896684029731904705327195985803679144228363491954477527965873183298008253342044909666678928652621870609847876094321918377736625397328779718636360329186588290565930395204711815558484311767794069673007474348582406231978151443386977502716889769008702219904513138015776347787820237315608190861564584447164485959753984940111737205571665450227347732963007698112580965846382037684936612460625547568778761106262868891235444910927914534830769251937491629784509624487023783492555209684120802640557526145055204013599100877237609938048819675715429998542683158060641181167013805793301541290463731752373194150117051635613";
        assertEquals(expectedModulus, pubKey.getModulus().toString());
    } 

    @Test
    void testUserCert() {
        X509CertificateHandler userCert = null;
        try {
            userCert = new X509CertificateHandler(new FileInputStream ("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/user01-x509.crt"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("ERROR");
        }
        if(userCert != null){
            String email = userCert.getEmail();
            assertTrue(email.equals("user01@inf1416.puc-rio.br"));
            String name = userCert.getName();
            assertTrue(name.equals("Usuario01"));
        }

    }
    @Test
    void testCertFromDb() {
        X509CertificateHandler userCert = null;

        try {
            FileInputStream is = new FileInputStream ("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/user01-x509.crt");
            userCert = new X509CertificateHandler(is);
            is.close();
            ByteArrayInputStream fi = new ByteArrayInputStream(userCert.getEncoded());
            String cert = new String(Base64.getEncoder().encode(fi.readAllBytes()), StandardCharsets.UTF_8);
            userCert = new X509CertificateHandler(cert);
            System.out.println(userCert.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("ERROR");
        }
    }
    

}
