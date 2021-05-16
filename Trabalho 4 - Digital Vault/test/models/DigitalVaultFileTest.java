package test.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import test.controllers.PrivateKeyHandlerTest;

import test.controllers.PrivateKeyHandlerTest;
import test.controllers.X509CertificateHandlerTest;
import models.DigitalVaultFile;
import org.junit.jupiter.api.Test;

public class DigitalVaultFileTest {
    

    @Test
    public void testDigitalFileDecrypt() {
        PrivateKey userPrivateKey = PrivateKeyHandlerTest.getUserPrivateKey();
        PublicKey userPublicKey = X509CertificateHandlerTest.getUserPublicKey();
        DigitalVaultFile dvf = new DigitalVaultFile("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/files/XXYYZZ11.env", "C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/files/XXYYZZ11.enc", "C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/files/XXYYZZ11.asd");
        try {
            byte[] decrpted = dvf.decrypt(userPrivateKey);
            assertEquals(true, dvf.isFileValid(decrpted, userPublicKey));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error");
        }
    }
}
