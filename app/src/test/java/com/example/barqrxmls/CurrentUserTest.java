package com.example.barqrxmls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

// Note, Current User simply holds a user from the database and allows other classes
// to interact with it without having to pass a user through intent or manually
// update the database for any changes to the user
// for this reason Current User only calls the super of it's methods, and a method which
// updates the database (which cannot be tested)
// all of CurrentUser's superClass methods are tested in UserUnitTest

/**
 * tests Current User
 */
public class CurrentUserTest {

    /**
     * tests creation of currentUser
     */
    @Test
    public void CurrentUserCreation() {
        CurrentUser c = CurrentUser.getInstance();
        assertNotNull(c);
    }

    /**
     * tests that the protected methods from current user's superclass are correctly accessed
     */
    @Test
    public void ProtectedMethods() {
        CurrentUser c = CurrentUser.getInstance();
        c.setTotalPoints(5);
        assertEquals(5,c.getTotalPoints());
        c.setNumCodes(2);
        assertEquals(2,c.getNumCodes());
        HashMap<String, HashMap<String,String>> test = new HashMap<>();
        HashMap<String,String> innerTest = new HashMap<>();
        innerTest.put("comment","comment");
        test.put("code",innerTest);
        c.setCodes(test);
        assertEquals(test,c.getCodes());
    }
}
