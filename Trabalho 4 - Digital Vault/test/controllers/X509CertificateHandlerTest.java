package test.controllers;

import controllers.X509CertificateHandler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import test.controllers.PrivateKeyHandlerTest;
import org.junit.jupiter.api.Test;


public class X509CertificateHandlerTest {

    static PublicKey getAdminPublicKey() {
        X509CertificateHandler adminCert = null;
        PublicKey pubK = null;
        try {
            adminCert = new X509CertificateHandler("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/admin-x509.crt");
            pubK = adminCert.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubK;
    }
    public static PublicKey getUserPublicKey() {
        X509CertificateHandler adminCert = null;
        PublicKey pubK = null;
        try {
            adminCert = new X509CertificateHandler("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/user01-x509.crt");
            pubK = adminCert.getPublicKey();
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

}