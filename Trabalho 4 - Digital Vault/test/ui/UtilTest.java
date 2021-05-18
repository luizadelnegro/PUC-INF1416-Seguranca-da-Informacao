package test.ui;

import ui.MyUtil;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UtilTest {

    @Test
    public void testStringRegex() {
        String email = "\"myemail@gmail.com::  ('a";
        System.out.println(MyUtil.sanitizeString(email));
    }
    
}
