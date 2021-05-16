package test.controllers;

import controllers.PrivateKeyHandler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import test.controllers.X509CertificateHandlerTest;

import org.junit.jupiter.api.Test;

public class PrivateKeyHandlerTest {
    
    public static PrivateKey getAdminPrivateKey() {
        PrivateKeyHandler adminPrivateK = new PrivateKeyHandler("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/admin-pkcs8-des.key");
        return adminPrivateK.getPrivateKey("admin");
    }

    public static PrivateKey getUserPrivateKey() {
        PrivateKeyHandler adminPrivateK = new PrivateKeyHandler("C:/Users/Lucas/Documents/GitHub/PUC-INF1416-Seguranca-da-Informacao/Trabalho 4 - Digital Vault/keys/user01-pkcs8-des.key");
        return adminPrivateK.getPrivateKey("user01");
    }

    @Test
    void testAdminPrivateKey() {
        PrivateKey adminPrivateK = getAdminPrivateKey();
        assertNotEquals(null, adminPrivateK);
        RSAPrivateKey privateAdminK = (RSAPrivateKey) adminPrivateK;
        String expected_private = "16200761980115475683328856108847563840420360785603529126090342269479724697741701097890370643928145330689831352165500548280747835278556940490992124815032276612499463868545740228295872324182324991752083647795763558899816905808076363295671500034016092827235441841170553158810874835179171019475785802921906240876029173917244925642099233758261754240123735953152715743296085976899658325861846493395366031764056348531290248754512797449596146144384900841443473841982261526418515608323420814695627893257274639899264555419103254649197182723975460527889915313081263419643312040645336627788847123771264210789622236880071696619553";
        assertEquals(expected_private, privateAdminK.getPrivateExponent().toString());
    }

    @Test
    void testValidPrivateKey() {
        Boolean valid = false;
        try{
            valid = PrivateKeyHandler.isPrivateKeyValid(getAdminPrivateKey(), X509CertificateHandlerTest.getAdminPublicKey());
        } catch(Exception e){
            e.printStackTrace();
            fail("Nao foi possivel comparar as chaves");
        }
        assertEquals(true, valid);
    }
    
}
