package com.example.d308_app;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.d308_app.UI.PasswordUtils;

public class PasswordUnitTest {
    @Test
    public void passwordIsCorrect(){
        String testPassword = "password1$";
        boolean test = PasswordUtils.isValidPassword(testPassword);

        //Should return true so long as testPassword meets the requirements of isValidPassword
        assertTrue(test);
    }

    @Test
    public void passwordIsIncorrect(){
        String testPassword = "password";
        boolean test = PasswordUtils.isValidPassword(testPassword);

        //should return false since testPassword does not meet requirements of isValidPassword
        assertFalse(test);
    }
}