package com.example.d308_app;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.d308_app.UI.PasswordUtils;

public class PasswordVerifyUnitTest {
    @Test
    public void passwordVerifyIsCorrect(){
        String testPass1 = "Password1?";
        String testPass2 = PasswordUtils.hashPassword("Password1?");

        boolean test = PasswordUtils.verifyPassword(testPass1, testPass2);

        //should return true so long as the passwords match
        assertTrue(test);
    }

    @Test
    public void passwordVerifyIsIncorrect(){
        String testPass1 = "Password12!?";
        String testPass2 = PasswordUtils.hashPassword("Passwor13!?");

        //should return false so long as passwords do not match
        boolean test = PasswordUtils.verifyPassword(testPass1, testPass2);

        assertFalse(test);
    }
}
